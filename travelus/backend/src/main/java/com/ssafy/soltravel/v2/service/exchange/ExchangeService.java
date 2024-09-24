package com.ssafy.soltravel.v2.service.exchange;


import com.ssafy.soltravel.v2.common.Header;
import com.ssafy.soltravel.v2.config.RabbitMQConfig;
import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.Enum.TransferType;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateCacheDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateRegisterRequestDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateResponseDto;
import com.ssafy.soltravel.v2.dto.exchange.targetAccountDto;
import com.ssafy.soltravel.v2.dto.transaction.request.MoneyBoxTransferRequestDto;
import com.ssafy.soltravel.v2.repository.GroupRepository;
import com.ssafy.soltravel.v2.service.transaction.TransactionService;
import com.ssafy.soltravel.v2.util.LogUtil;
import com.ssafy.soltravel.v2.util.WebClientUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

  private final String BASE_URL = "http://localhost:8080/api/v1/bank/exchange";
  private final Map<String, String> apiKeys;
  private final WebClientUtil webClientUtil;
  private final ModelMapper modelMapper;

  private final CacheManager cacheManager;
  private final RedisTemplate<String, String> redisTemplate;
  private final TransactionService transactionService;
  private final GroupRepository groupRepository;

  private List<String> Currencies = List.of("USD", "JPY", "EUR", "CNY");

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

    //TODO: 매퍼 생성할 것
    ExchangeRateResponseDto responseDto = new ExchangeRateResponseDto();
    responseDto.setCurrencyCode(currency);

    ExchangeRateCacheDto dto = getExchangeRateFromCache(currency);
    responseDto.setExchangeRate(dto.getExchangeRate());
    responseDto.setCreated(dto.getCreated());
    responseDto.setExchangeMin(dto.getExchangeMin());
    return responseDto;
  }

  /**
   * 목표 환율 설정
   */
  public void setPreferenceRate(ExchangeRateRegisterRequestDto dto) {

    String accountNo = dto.getAccountNo();//계좌번호
    CurrencyType currencyCode = dto.getCurrencyCode();
    double targetRate = BigDecimal.valueOf(dto.getTargetRate()).setScale(2, RoundingMode.HALF_UP)
        .doubleValue();
    double amount = dto.getTransactionBalance();

    long userId = groupRepository.findMasterUserIdByAccountNo(dto.getAccountNo());

    // Redis ZSET 키 생성: 예를 들어 "USD:targets"
    String key = currencyCode + ":targets";

    // 계좌 번호, 사용자 ID, 금액, 환율을 결합한 문자열 생성
    String value = userId + ":" + accountNo + ":" + amount + ":" + targetRate;

    // ZSET에 (사용자 ID:계좌 번호:금액:환율)을 저장하고, score로 목표 환율을 설정
    redisTemplate.opsForZSet().add(key, value, targetRate);

    // 필요한 경우 TTL 설정 (아래는 1일임)
    redisTemplate.expire(key, Duration.ofDays(1));
//    LogUtil.info("자동환전 계좌 목록 조회::", getAccountsForRateHigherThan("USD", 1331));
  }

  /**
   * 자동 환전
   */
  private void processCurrencyConversions(String currencyCode, Double exchangeRate) {

    Set<targetAccountDto> list = getAccountsForRateHigherThan(currencyCode, exchangeRate);
    LogUtil.info("자동환전 계좌 목록 조회::", getAccountsForRateHigherThan(currencyCode, exchangeRate));
    //[[targetAccountDto(accountNo=002-45579486-209, userId=1, amount=130000.0, targetRate=1335.91)]]

    for (targetAccountDto dto : list) {

      MoneyBoxTransferRequestDto requestDto = MoneyBoxTransferRequestDto.create(TransferType.M,
          dto.getAccountNo(),null, CurrencyType.KRW, getCurrencyType(currencyCode),
          String.valueOf(dto.getAmount()));

      try {
        // 환전 로직 호출
        transactionService.postMoneyBoxTransfer(requestDto, true, dto.getUserId());
      } catch (WebClientResponseException e) {
        //잔액부족시
        if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
          LogUtil.error("자동환전 실패. 계좌 번호: {}, 사용자 ID: {}, 에러: INSUFFICIENT_BALANCE", dto.getAccountNo(), dto.getUserId());

        } else {
          LogUtil.error("자동환전 실패. 계좌 번호: {}, 사용자 ID: {}, 에러: {}", dto.getAccountNo(), dto.getUserId(), e.getMessage());
        }

        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      } catch (Exception e) {
        LogUtil.error("자동환전 실패. 계좌 번호: {}, 사용자 ID: {}, 에러: {}", dto.getAccountNo(), dto.getUserId(), e.getMessage());
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      }
    }
  }

  /**
   * 실시간 환율 <= 목표 환율인 계좌 목록 조회
   */
  public Set<targetAccountDto> getAccountsForRateHigherThan(String currencyCode,
      double realTimeRate) {
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
        double targetRate = Double.parseDouble(parts[3]);

        // AccountWithAmount 객체 생성 후 리스트에 추가
        accounts.add(new targetAccountDto(accountNo, userId, amount, targetRate));
      }
    }
    return accounts;
  }

  /**
   * String의 currencyCode를 CurreucyType으로 변환
   */
  private CurrencyType getCurrencyType(String currencyCode) {

    return switch (currencyCode) {
      case "USD" -> CurrencyType.USD;
      case "JPY" -> CurrencyType.JPY;
      case "EUR" -> CurrencyType.EUR;
      case "CNY" -> CurrencyType.CNY;
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
          new ExchangeRateCacheDto(currencyCode, exchangeRate, timeLastUpdateUtc,String.valueOf(exchangeMin)));

      //TODO: 주석 풀 것
      //자동환전
//      processCurrencyConversions(currencyCode, exchangeRate);
    } else {
      LogUtil.info("환율 변동 없음. 통화 코드: {}, 기존 환율: {}, 새로운 환율: {}", currencyCode,
          cachedDto.getExchangeRate(), exchangeRate);
    }
    /**
     * 코컬 테스트용
     */
//    LogUtil.info("자동환전시작");
    processCurrencyConversions(currencyCode, exchangeRate);
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
        BASE_URL + "/" + currencyCode, body, Map.class);
    Object recObject = response.getBody().get("REC");

    ExchangeRateCacheDto rateDto = modelMapper.map(recObject,
        ExchangeRateCacheDto.class);
    if (rateDto.getCurrencyCode().equals(currencyCode)) {
      return rateDto; // 환율 반환
    }
    return null;

//    ResponseEntity<ExchangeRateCacheDto> response = webClient.get()
//        .uri("/exchange/" + currencyCode).retrieve().toEntity(ExchangeRateCacheDto.class).block();
//
//    if (response != null && response.getBody() != null) {
//      ExchangeRateCacheDto rateDto = response.getBody();
//      if (rateDto.getCurrencyCode().equals(currencyCode)) {
//        return rateDto; // 환율 반환
//      }
//    }
//    return null; // 실패 시 null 반환
  }
}
