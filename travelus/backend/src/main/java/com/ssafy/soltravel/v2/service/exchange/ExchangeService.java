package com.ssafy.soltravel.v2.service.exchange;


import com.ssafy.soltravel.v2.common.Header;
import com.ssafy.soltravel.v2.config.RabbitMQConfig;
import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.Enum.TransferType;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateCacheDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateRegisterRequestDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateResponseDto;
import com.ssafy.soltravel.v2.dto.exchange.targetAccountDto;
import com.ssafy.soltravel.v2.dto.moneyBox.BalanceResponseDto;
import com.ssafy.soltravel.v2.dto.transaction.request.MoneyBoxTransferRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.response.TransferHistoryResponseDto;
import com.ssafy.soltravel.v2.mapper.ExchangeRateMapper;
import com.ssafy.soltravel.v2.repository.ExchangeRateForecastRepository;
import com.ssafy.soltravel.v2.repository.GroupRepository;
import com.ssafy.soltravel.v2.service.transaction.TransactionService;
import com.ssafy.soltravel.v2.util.LogUtil;
import com.ssafy.soltravel.v2.util.WebClientUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Transactional
public class ExchangeService {

  private final String BASE_URL = "/exchange/";
  private final Map<String, String> apiKeys;
  private final WebClientUtil webClientUtil;
  private final ModelMapper modelMapper;
  private final ExchangeRateMapper exchangeRateMapper;

  private final CacheManager cacheManager;
  private final RedisTemplate<String, String> redisTemplate;
  private final TransactionService transactionService;
  private final GroupRepository groupRepository;
  private final ExchangeRateForecastRepository exchangeRateForecastRepository;

  private List<String> Currencies = List.of("USD", "JPY", "EUR", "TWD");

  /**
   * 현재 환율 전체 조회
   */
  public List<ExchangeRateResponseDto> getExchangeRateAll() {

    List<ExchangeRateResponseDto> rateEntity = new ArrayList<>();
    for (String currency : Currencies) {

      rateEntity.add(getExchangeRate(currency));
    }
    return rateEntity;
  }


  /**
   * 현재 환율 단건 조회
   */
  public ExchangeRateResponseDto getExchangeRate(String currency) {

    ExchangeRateResponseDto exchangeRateResponseDto = exchangeRateMapper.toExchangeRateResponseDto(
        getExchangeRateFromCache(currency));
    return exchangeRateResponseDto;
  }

  /**
   * 목표 환율 설정
   */
  public void setPreferenceRate(ExchangeRateRegisterRequestDto dto) {

    String accountNo = dto.getAccountNo();
    CurrencyType currencyCode = dto.getCurrencyCode();
    double targetRate = BigDecimal.valueOf(dto.getTargetRate()).setScale(2, RoundingMode.HALF_UP)
        .doubleValue();

    // amount가 null이면 잔액 전체 환전으로 -1로 처리
    Double amount = dto.getTransactionBalance() != null ? dto.getTransactionBalance() : -1.0;
    long userId = groupRepository.findMasterUserIdByAccountNo(dto.getAccountNo());

    String key = currencyCode + ":targets";
    String value = userId + ":" + accountNo + ":" + amount + ":" + targetRate;

    redisTemplate.opsForZSet().add(key, value, targetRate);

    if (dto.getDueDate() != null) {
      LocalDate dueDate = dto.getDueDate();
      LocalDateTime endOfDay = dueDate.atTime(LocalTime.of(23, 59, 59));
      LocalDateTime now = LocalDateTime.now();

      if (endOfDay.isAfter(now)) {
        Duration duration = Duration.between(now, endOfDay);
        long ttlInSeconds = duration.getSeconds();
        redisTemplate.expire(key, ttlInSeconds, TimeUnit.SECONDS);
      } else {
        redisTemplate.expire(key, 0, TimeUnit.SECONDS);
      }
    }
  }

  /**
   * 자동 환전
   */
  private void processCurrencyConversions(String currencyCode, Double exchangeRate) {

    Set<targetAccountDto> list = getAccountsForRateHigherThan(currencyCode, exchangeRate);
    for (targetAccountDto dto : list) {

      MoneyBoxTransferRequestDto requestDto = MoneyBoxTransferRequestDto.create(TransferType.M,
          dto.getAccountNo(), null, CurrencyType.KRW, getCurrencyType(currencyCode),
          String.valueOf(dto.getAmount()));

      try {
        // 환전 로직 호출
        List<TransferHistoryResponseDto> transferHistoryResponseDtos = transactionService.postMoneyBoxTransfer(requestDto, true,
            dto.getUserId()).getBody();

        double amount = dto.isAll() ? -1 : dto.getAmount();

        removePreferenceRateFromRedis(currencyCode, dto.getUserId(), dto.getAccountNo(), amount, dto.getTargetRate());
        LogUtil.info("자동환전 성공. 환전 신청 원화: %s, 적용 환율: %s, 환전된 금액: %s ", transferHistoryResponseDtos.get(0).getTransactionAmount(),
            transferHistoryResponseDtos.get(1).getTransactionSummary(),
            transferHistoryResponseDtos.get(1).getTransactionAmount());

      } catch (WebClientResponseException e) {
        //잔액부족시
        if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
          LogUtil.error("자동환전 실패. 계좌 번호: {}, 사용자 ID: {}, 에러: INSUFFICIENT_BALANCE",
              dto.getAccountNo(), dto.getUserId());

        } else {
          LogUtil.error("자동환전 실패. 계좌 번호: {}, 사용자 ID: {}, 에러: {}", dto.getAccountNo(),
              dto.getUserId(), e.getMessage());
        }

        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      } catch (Exception e) {
        LogUtil.error("자동환전 실패. 계좌 번호: {}, 사용자 ID: {}, 에러: {}", dto.getAccountNo(), dto.getUserId(),
            e.getMessage());
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      }
    }
  }

  /**
   * 실시간 환율 <= 목표 환율인 계좌 목록 조회
   */
  public Set<targetAccountDto> getAccountsForRateHigherThan(String currencyCode,
      double realTimeRate) {
    boolean isAll = false;
    String key = currencyCode + ":targets";

    // 실시간 환율보다 높은 모든 계좌 ID와 금액을 조회 (ZSET에서 score가 실시간 환율보다 큰 요소들을 가져옴)
    Set<String> results = redisTemplate.opsForZSet()
        .rangeByScore(key, realTimeRate, Double.MAX_VALUE);

    // 분리하여 AccountWithAmount 객체로 변환
    Set<targetAccountDto> accounts = new HashSet<>();
    if (results != null) {
      for (String result : results) {

        String[] parts = result.split(":");
        long userId = Long.parseLong(parts[0]);
        String accountNo = parts[1];
        double amount = Double.parseDouble(parts[2]);
        if (amount == -1) {

          amount = getKRWBalanceByAccountNo(accountNo);
          isAll = true;
        }

        double targetRate = Double.parseDouble(parts[3]);
        accounts.add(new targetAccountDto(accountNo, userId, amount, targetRate, isAll));
      }
    }
    return accounts;
  }

  /**
   * Redis에서 값 삭제하는 메서드
   */
  public void removePreferenceRateFromRedis(String currencyCode, long userId, String accountNo, double amount,
      double targetRate) {
    String key = currencyCode + ":targets";
    String value = userId + ":" + accountNo + ":" + amount + ":" + targetRate;

    redisTemplate.opsForZSet().remove(key, value);
  }


  /**
   * String의 currencyCode를 CurreucyType으로 변환
   */
  public CurrencyType getCurrencyType(String currencyCode) {

    return switch (currencyCode) {
      case "USD" -> CurrencyType.USD;
      case "JPY" -> CurrencyType.JPY;
      case "EUR" -> CurrencyType.EUR;
      case "TWD" -> CurrencyType.TWD;
      default -> CurrencyType.KRW;
    };
  }

  /**
   * ----------------------아래부터는 은행서버와 통신을 위한 메서드----------------------
   */
  /**
   * RabbitMQ 메시지 수신
   */
  @RabbitListener(queues = RabbitMQConfig.EXCHANGE_RATE_QUEUE)
  public void receiveMessage(String message) {
    // 메시지 파싱
    String[] parts = message.split(", ");
    String currencyCode = parts[0].split(": ")[1];
    Double exchangeRate = Double.parseDouble(parts[1].split(": ")[1]);
    String timeLastUpdateUtc = parts[2].split(": ")[1];
    int exchangeMin = Integer.parseInt(parts[3].split(": ")[1]);

    // 이전 환율 가져오기
    ExchangeRateCacheDto cachedDto = getExchangeRateFromCache(currencyCode);

    // 환율이 변경되었는지 체크
    if (cachedDto == null || !exchangeRate.equals(cachedDto.getExchangeRate())) {
      // 캐시에 새로운 환율 저장
      updateExchangeRateCache(
          new ExchangeRateCacheDto(currencyCode, exchangeRate, timeLastUpdateUtc,
              String.valueOf(exchangeMin)));

      processCurrencyConversions(currencyCode, exchangeRate);
    } else {
      LogUtil.info(String.format("환율 변동 없음. 통화 코드: {}, 기존 환율: {}, 새로운 환율: {}"), currencyCode,
          cachedDto.getExchangeRate(), exchangeRate);
    }
    /**
     * 코컬 테스트용
     */
//    processCurrencyConversions(currencyCode, exchangeRate);
  }

  /**
   * 환율 캐시 업데이트
   */
  @CachePut(value = "exchangeRates", key = "#dto.currencyCode")
  public void updateExchangeRateCache(ExchangeRateCacheDto dto) {

    Cache cache = cacheManager.getCache("exchangeRates");
    if (cache != null) {
      cache.put(dto.getCurrencyCode(), dto);
    }
  }

  /**
   * cache에서 환율 조회
   */
  @Cacheable(value = "exchangeRates", key = "#currencyCode")
  public ExchangeRateCacheDto getExchangeRateFromCache(String currencyCode) {

    Cache cache = cacheManager.getCache("exchangeRates");
    if (cache != null) {
      ExchangeRateCacheDto cachedDto = cache.get(currencyCode, ExchangeRateCacheDto.class);
      if (cachedDto != null) {
        return cachedDto;
      }
    }

    // 외부 API에서 환율 정보 가져오기
    Header header = Header.builder()
        .apiKey(apiKeys.get("API_KEY")).build();

    Map<String, Object> body = new HashMap<>();
    body.put("Header", header);

    ResponseEntity<Map<String, Object>> response = webClientUtil.request(
        BASE_URL + currencyCode, body, Map.class);
    Object recObject = response.getBody().get("REC");

    ExchangeRateCacheDto rateDto = modelMapper.map(recObject,
        ExchangeRateCacheDto.class);
    if (rateDto.getCurrencyCode().equals(currencyCode)) {
      return rateDto; // 환율 반환
    }
    return null;
  }

  /**
   * 계좌 잔액 조회 메서드
   */
  public double getKRWBalanceByAccountNo(String accountNo) {
    Header header = Header.builder()
        .apiKey(apiKeys.get("API_KEY")).build();

    Map<String, Object> body = new HashMap<>();
    body.put("Header", header);
    body.put("accountNo", accountNo);
    body.put("currencyCode", "KRW");

    ResponseEntity<Map<String, Object>> response = webClientUtil.request(
        "/accounts/balance", body, Map.class);
    Object recObject = response.getBody().get("REC");

    BalanceResponseDto responseDto = modelMapper.map(recObject,
        BalanceResponseDto.class);

    LogUtil.info("계좌 잔액 responseDto.balance", responseDto.getBalance());
    return Double.parseDouble(responseDto.getBalance());
  }
}
