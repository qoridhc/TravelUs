package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.config.RabbitMQConfig;
import com.goofy.tunabank.v1.domain.Currency;
import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.dto.exchange.ExchangeRateCacheDTO;
import com.goofy.tunabank.v1.repository.CurrencyRepository;
import com.goofy.tunabank.v1.util.LogUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
public class ExchangeService {

  private final WebClient ExchangewebClient;
  private final CurrencyRepository currencyRepository;
  private final RabbitTemplate rabbitTemplate;
  private final RabbitMQConfig rabbitMQConfig;


  private List<String> desiredCurrencies = List.of("USD", "JPY", "EUR", "CNY");

  //  @Scheduled(cron = "30 0 * * * ?")
  public List<ExchangeRateCacheDTO> updateExchangeRates() {
    String url = "/latest/KRW";

    String response = ExchangewebClient.get().uri(url).retrieve().bodyToMono(String.class)
        .block();

    // JSON 파싱
    JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
    JsonObject conversionRates = jsonObject.getAsJsonObject("conversion_rates");

    // 결과를 저장할 리스트
    List<ExchangeRateCacheDTO> cacheDTOList = new ArrayList<>();

    for (String currencyCode : desiredCurrencies) {
      if (conversionRates.has(currencyCode)) {
        double rate = conversionRates.get(currencyCode).getAsDouble();

        // JPY는 100 단위로 계산
        if (currencyCode.equals("JPY")) {
          rate = Math.round((1 / rate) * 10000.0) / 100.0;  // 소수점 둘째 자리까지 반올림
        } else {
          rate = Math.round((1 / rate) * 100.0) / 100.0;    // 다른 통화는 소수점 둘째 자리까지 반올림
        }

        // DTO 객체 생성 후 리스트에 추가
        ExchangeRateCacheDTO dto = new ExchangeRateCacheDTO(currencyCode, rate);
        cacheDTOList.add(dto);

        // 환율 업데이트
        saveOrUpdateCurrency(getCurrencyType(currencyCode), rate);

        // RabbitMQ로 메시지 전송
        String message = "Currency: " + currencyCode + ", Rate: " + rate;
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_RATE_QUEUE, message);
        LogUtil.info("Sent message to TravelUs: {}", message);
      }
    }
    return cacheDTOList;
  }

  /**
   * CurrencyRepository에서 환율 단건 조회 (TravelUs 캐싱용 DTO반환)
   */
  public ExchangeRateCacheDTO getExchangeRateByCurrencyCode(String currencyCode) {

    Currency response = currencyRepository.findByCurrencyCode(getCurrencyType(currencyCode));

    return ExchangeRateCacheDTO.builder()
        .CurrencyCode(currencyCode)
        .ExchangeRate(response.getExchangeRate())
        .build();
  }

  /**
   * 환율 업데이트
   */
  private void saveOrUpdateCurrency(CurrencyType type, Double exchangeRate) {

    Currency existingCurrency = currencyRepository.findByCurrencyCode(type);

    existingCurrency.setExchangeRate(exchangeRate);
    currencyRepository.save(existingCurrency);
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
}