package com.goofy.tunabank.service;

import com.goofy.tunabank.domain.Currency;
import com.goofy.tunabank.domain.Enum.CurrencyType;
import com.goofy.tunabank.repository.CurrencyRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ExchangeService {

  private final Map<String, String> apiKeys;
  private final WebClient ExchangewebClient;

  private final CurrencyRepository currencyRepository;


  private List<String> desiredCurrencies = List.of("USD", "JPY", "EUR", "CNY");

//  @Scheduled(cron = "30 0 * * * ?")
  public String updateExchangeRates() {
    String url = "/latest/KRW";

    String response = ExchangewebClient.get().uri(url).retrieve().bodyToMono(String.class)
        .block(); // 동기적으로 호출

    // JSON 파싱
    JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
    // conversion_rates만 추출
    JsonObject conversionRates = jsonObject.getAsJsonObject("conversion_rates");

    for (String currencyCode : desiredCurrencies) {
      if (conversionRates.has(currencyCode)) {
        double rate = conversionRates.get(currencyCode).getAsDouble();

        // JPY는 100 단위로 계산
        if (currencyCode.equals("JPY")) {
          rate = Math.round((1 / rate) * 10000.0) / 100.0;  // 소수점 둘째 자리까지 반올림
        } else {
          rate = Math.round((1 / rate) * 100.0) / 100.0;    // 다른 통화는 소수점 둘째 자리까지 반올림
        }

        // 환율 저장 또는 업데이트
        saveOrUpdateCurrency(getCurrencyType(currencyCode), rate);
      }
    }
    return "success";
  }

  private void saveOrUpdateCurrency(CurrencyType type, Double exchangeRate) {

    Currency existingCurrency = currencyRepository.findByCurrencyCode(type);

    existingCurrency.setExchangeRate(exchangeRate);
    currencyRepository.save(existingCurrency);
  }

  private CurrencyType getCurrencyType(String currencyCode) {

    CurrencyType type = CurrencyType.KRW;
    switch (currencyCode) {
      case "USD" -> type = CurrencyType.USD;
      case "JPY" -> type = CurrencyType.JPY;
      case "EUR" -> type = CurrencyType.EUR;
      case "CNY" -> type = CurrencyType.CNY;
    }
    return type;
  }
}
