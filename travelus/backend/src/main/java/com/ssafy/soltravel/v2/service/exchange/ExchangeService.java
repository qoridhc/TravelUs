package com.ssafy.soltravel.v2.service.exchange;


import com.ssafy.soltravel.v2.config.RabbitMQConfig;
import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateCacheDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateRegisterRequestDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateResponseDto;
import com.ssafy.soltravel.v2.dto.exchange.targetAccountDto;
import com.ssafy.soltravel.v2.util.LogUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
public class ExchangeService {

  private final WebClient webClient;
  private final CacheManager cacheManager;
  private final RedisTemplate<String, String> redisTemplate;

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
    responseDto.setCreated(dto.getLastUpdatedTime());

    //TODO:최소금액
//    responseDto.setExchangeMin(rateEntity.getExchangeMin());
    return responseDto;
  }

  /**
   * 목표 환율 설정
   */
  public void setPreferenceRate(ExchangeRateRegisterRequestDto dto) {

    long accountId = dto.getAccountId();
    CurrencyType currencyCode = dto.getCurrencyCode();
    double targetRate = BigDecimal.valueOf(dto.getTargetRate()).setScale(2, RoundingMode.HALF_UP)
        .doubleValue();
    double amount = dto.getTransactionBalance();
    // Redis ZSET 키 생성: 예를 들어 "USD:targets"
    String key = currencyCode + ":targets";

    // // 계좌 ID, 금액, 환율을 결합한 문자열 생성
    String value = accountId + ":" + amount + ":" + targetRate;

    // ZSET에 계좌 ID:금액을 저장하고, score로 목표 환율을 설정
    redisTemplate.opsForZSet().add(key, value, targetRate);

    // 필요한 경우 TTL 설정 (아래는 1일임)
    redisTemplate.expire(key, Duration.ofDays(1));
  }

  /**
   * 자동 환전
   */
  private void processCurrencyConversions(String currencyCode, Double exchangeRate) {

    getAccountsForRateHigherThan(currencyCode, exchangeRate);
    LogUtil.info("계좌 목록 조회::", getAccountsForRateHigherThan(currencyCode, exchangeRate));
    //[[targetAccountDto(accountId=2, amount=130000.0, targetRate=1335.9)]]
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

    // 계좌 ID와 금액을 분리하여 AccountWithAmount 객체로 변환
    Set<targetAccountDto> accounts = new HashSet<>();
    if (results != null) {
      for (String result : results) {
        String[] parts = result.split(":");
        long accountId = Long.parseLong(parts[0]);
        double amount = Double.parseDouble(parts[1]);
        double targetRate = Double.parseDouble(parts[2]);

        // AccountWithAmount 객체 생성 후 리스트에 추가
        accounts.add(new targetAccountDto(accountId, amount, targetRate));
      }
    }
    return accounts;
  }

//  /**
//   * 실시간 환율 데이터 db에 저장
//   */
//  public void updateExchangeRates(List<ExchangeRateDto> dtoList) {
//    for (ExchangeRateDto dto : dtoList) {
//
//      ExchangeRate rate = exchangeRateRepository.findByCurrencyCode(dto.getCurrency());
//      double prevRate = -1D;
//
//      if (rate != null) {
//        //이전 환율 저장
//        prevRate = rate.getExchangeRate();
//      } else {
//        rate = ExchangeRate.builder().currencyCode(dto.getCurrency()).build();
//      }
//
//      double updatedRate = getDoubleExchangeRate(dto.getExchangeRate());
//
//      rate = rate.toBuilder().exchangeRate(updatedRate)
//          .exchangeMin(Double.parseDouble(dto.getExchangeMin()))
//          .created(getLocalDateTime(dto.getCreated())).build();
//      exchangeRateRepository.save(rate);
//
//      /**
//       * 환율이 변동되었다 -> 자동 환전
//       */
//      if (prevRate != updatedRate) {
//        // ID에 등록된 account를 가져온다
//        String id = makeId(dto.getCurrency(), updatedRate);
//        Optional<PreferenceRate> exchangeOpt = preferenceRateRepository.findById(id);
//
//        if (exchangeOpt.isPresent()) {
//          PreferenceRate preferenceRate = exchangeOpt.get();
//
//          for (Account account : preferenceRate.getAccounts()) {
//
//            GeneralAccount generalAccount = generalAccountRepository.findById(
//                account.getAccountId()).orElseThrow();
//
//            ExchangeRequestDto exchangeRequestDto = ExchangeRequestDto.builder()
//                .currencyCode(dto.getCurrency()).accountId(account.getAccountId())
//                .accountNo(account.getAccountNo())
//                .exchangeAmount(Math.round(generalAccount.getBalance()))//모임계좌 전액 환전
//                .exchangeRate(updatedRate).build();
//
//            LogUtil.info("<<자동 환전>> 모임 계좌 번호:", generalAccount.getAccountNo());
//            LogUtil.info("<<자동 환전>> 계좌 잔액:", generalAccount.getBalance());
////            executeKRWTOUSDExchange(exchangeRequestDto);
//          }
//        }
//      }
//    }
//  }

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

    // 이전 환율 가져오기
    ExchangeRateCacheDto cachedDto = getExchangeRateFromCache(currencyCode);

    // 환율이 변경되었는지 체크
    if (cachedDto == null || !exchangeRate.equals(cachedDto.getExchangeRate())) {
      // 캐시에 새로운 환율 저장
      updateExchangeRateCache(
          new ExchangeRateCacheDto(currencyCode, exchangeRate, timeLastUpdateUtc));

      // 대기열에 있는 계좌들을 자동으로 환전 처리
      processCurrencyConversions(currencyCode, exchangeRate);
    } else {
      LogUtil.info("환율 변동 없음. 통화 코드: {}, 기존 환율: {}, 새로운 환율: {}", currencyCode,
          cachedDto.getExchangeRate(), exchangeRate);
    }
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
    ResponseEntity<ExchangeRateCacheDto> response = webClient.get()
        .uri("/exchange/" + currencyCode).retrieve().toEntity(ExchangeRateCacheDto.class).block();

    if (response != null && response.getBody() != null) {
      ExchangeRateCacheDto rateDto = response.getBody();
      if (rateDto.getCurrencyCode().equals(currencyCode)) {
        return rateDto; // 환율 반환
      }
    }
    return null; // 실패 시 null 반환
  }
}
