package com.ssafy.soltravel.v2.service.exchange;


import com.ssafy.soltravel.v2.config.RabbitMQConfig;
import com.ssafy.soltravel.v2.domain.ExchangeRate;
import com.ssafy.soltravel.v2.domain.GeneralAccount;
import com.ssafy.soltravel.v2.domain.LatestRate;
import com.ssafy.soltravel.v2.domain.redis.PreferenceRate;
import com.ssafy.soltravel.v2.dto.exchange.Account;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateCacheDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateRegisterRequestDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateResponseDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRequestDto;
import com.ssafy.soltravel.v2.dto.exchange.LatestRateRequestDto;
import com.ssafy.soltravel.v2.repository.ExchangeRateRepository;
import com.ssafy.soltravel.v2.repository.GeneralAccountRepository;
import com.ssafy.soltravel.v2.repository.LatestRateRepository;
import com.ssafy.soltravel.v2.repository.redis.PreferenceRateRepository;
import com.ssafy.soltravel.v2.util.LogUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
  private final ExchangeRateRepository exchangeRateRepository;
  private final PreferenceRateRepository preferenceRateRepository;
  private final LatestRateRepository latestRateRepository;
  private final GeneralAccountRepository generalAccountRepository;
  
  private List<String> Currencies = List.of("USD", "JPY", "EUR", "CNY");

  /**
   * 현재 환율 전체 조회
   */
  public List<ExchangeRateResponseDto> getExchangeRateAll() {

    List<ExchangeRateResponseDto> rateEntity = new ArrayList<>();
    for(String currency : Currencies) {

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

    ExchangeRateCacheDto dto=getExchangeRateFromCache(currency);
    responseDto.setExchangeRate(dto.getExchangeRate());
    responseDto.setCreated(dto.getLastUpdatedTime());

    //TODO:최소금액
//    responseDto.setExchangeMin(rateEntity.getExchangeMin());
    return responseDto;
  }

  /**
   * 최근 환율 조회
   */
  public List<LatestRate> getLatestExchangeRate(LatestRateRequestDto requestDto) {

    return latestRateRepository.findLatestRatesByCurrencyAndDateRange(requestDto.getCurrencyCode(),
        requestDto.getStartDate(), requestDto.getEndDate());
  }

  /**
   * 실시간 환율 데이터 db에 저장
   */
  public void updateExchangeRates(List<ExchangeRateDto> dtoList) {
    for (ExchangeRateDto dto : dtoList) {

      ExchangeRate rate = exchangeRateRepository.findByCurrencyCode(dto.getCurrency());
      double prevRate = -1D;

      if (rate != null) {
        //이전 환율 저장
        prevRate = rate.getExchangeRate();
      } else {
        rate = ExchangeRate.builder().currencyCode(dto.getCurrency()).build();
      }

      double updatedRate = getDoubleExchangeRate(dto.getExchangeRate());

      rate = rate.toBuilder().exchangeRate(updatedRate)
          .exchangeMin(Double.parseDouble(dto.getExchangeMin()))
          .created(getLocalDateTime(dto.getCreated())).build();
      exchangeRateRepository.save(rate);

      /**
       * 환율이 변동되었다 -> 자동 환전
       */
      if (prevRate != updatedRate) {
        // ID에 등록된 account를 가져온다
        String id = makeId(dto.getCurrency(), updatedRate);
        Optional<PreferenceRate> exchangeOpt = preferenceRateRepository.findById(id);

        if (exchangeOpt.isPresent()) {
          PreferenceRate preferenceRate = exchangeOpt.get();

          for (Account account : preferenceRate.getAccounts()) {

            GeneralAccount generalAccount = generalAccountRepository.findById(
                account.getAccountId()).orElseThrow();

            ExchangeRequestDto exchangeRequestDto = ExchangeRequestDto.builder()
                .currencyCode(dto.getCurrency()).accountId(account.getAccountId())
                .accountNo(account.getAccountNo())
                .exchangeAmount(Math.round(generalAccount.getBalance()))//모임계좌 전액 환전
                .exchangeRate(updatedRate).build();

            LogUtil.info("<<자동 환전>> 모임 계좌 번호:", generalAccount.getAccountNo());
            LogUtil.info("<<자동 환전>> 계좌 잔액:", generalAccount.getBalance());
//            executeKRWTOUSDExchange(exchangeRequestDto);
          }
        }
      }
    }
  }

  /**
   * 환전 선호 금액 설정
   */
  public void setPreferenceRate(String accountNo, ExchangeRateRegisterRequestDto dto) {

    String id = makeId(dto.getCurrencyCode(), dto.getExchangeRate());
    Optional<PreferenceRate> exchangeOpt = preferenceRateRepository.findById(id);

    PreferenceRate preference;
    if (exchangeOpt.isPresent()) {
      preference = exchangeOpt.get();
    } else {
      preference = new PreferenceRate(id, new HashSet<>());
    }

    preference.getAccounts().add(new Account(dto.getGeneralAccountId(), accountNo));
    preferenceRateRepository.save(preference);
  }


  /**
   * 아래부터는 형 변환 메서드 모음
   */
  public LocalDateTime getLocalDateTime(String str) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return LocalDateTime.parse(str, formatter);
  }

  public double getDoubleExchangeRate(String exchangeRate) {

    String exchangeRateStr = exchangeRate.replace(",", "");
    return Double.parseDouble(exchangeRateStr);
  }

  public String makeId(String currency, double rate) {
    return String.format("%s(%.2f)", currency, rate);
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

    // 캐시에 저장하는 로직 호출
    updateExchangeRateCache(new ExchangeRateCacheDto(currencyCode, exchangeRate,timeLastUpdateUtc));
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
  public ExchangeRateCacheDto  getExchangeRateFromCache(String currencyCode) {

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
