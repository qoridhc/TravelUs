package com.ssafy.soltravel.v2.service.exchange;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.ExchangeRateForecast;
import com.ssafy.soltravel.v2.dto.exchange.forecast.ExchangeRateSaveRequestDto;
import com.ssafy.soltravel.v2.repository.ExchangeRateForecastRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExchangeRateForecastService {

  private final ExchangeService exchangeService;
  private final ExchangeRateForecastRepository exchangeRateForecastRepository;

  //-----------------------------전체 저장-----------------------------
  public int parseExchangeRates(ExchangeRateSaveRequestDto request) {
    int length = 0;

    for (String currency : request.getCurrencies().keySet()) {

      // 통화코드 조회
      CurrencyType currencyType = exchangeService.getCurrencyType(currency);
      ExchangeRateSaveRequestDto.ExchangeRateData data = request.getCurrencies().get(currency);

      // 과거 저장
      Map<String, Double> recentRates = (Map<String, Double>) data.getRecentRates().get("3_months");
      length += saveExchangeRate(recentRates, currencyType);

      // 예측값 저장
      Map<String, Double> forecast = data.getForecast();
      length += saveExchangeRate(forecast, currencyType);

    }

    return length;
  }

  //-----------------------------예측값 저장-----------------------------
  public int parsePredExchangeRates(ExchangeRateSaveRequestDto request) {
    int length = 0;

    for (String currency : request.getCurrencies().keySet()) {
      CurrencyType currencyType = exchangeService.getCurrencyType(currency);
      Map<String, Double> forecast = request.getCurrencies().get(currency).getForecast();
      if(forecast == null) continue;

      length += updateExchangeRate(forecast, currencyType);
    }
    return length;
  }

  //-----------------------------환율 저장-----------------------------
  private int saveExchangeRate(Map<String, Double> data, CurrencyType currencyType) {
    if(data == null) return 0;
    List<ExchangeRateForecast> list = new ArrayList<>();

    //date별 rate 저장
    data.forEach((date, rate) -> {
      ExchangeRateForecast pred = ExchangeRateForecast.create(
          LocalDate.parse(date),
          currencyType,
          Double.valueOf(rate)
      );
      list.add(pred);
    });
    exchangeRateForecastRepository.save(list);
    return list.size();
  }

  //-----------------------------환율 수정-----------------------------
  private int updateExchangeRate(Map<String, Double> newValues, CurrencyType currencyType) {
    AtomicInteger length = new AtomicInteger();
    newValues.forEach((dateString, rate) -> {
      LocalDate date = LocalDate.parse(dateString);

      ExchangeRateForecast forecast = exchangeRateForecastRepository.findByDateAndCurrency(
          date,
          currencyType
      ).orElse(null);

      if(forecast != null) {
        forecast.updateRate(rate);
      }
      else{
        ExchangeRateForecast pred = ExchangeRateForecast.create(
            date,
            currencyType,
            rate
        );
        exchangeRateForecastRepository.save(pred);
      }

      length.getAndIncrement();
    });

    return length.get();
  }
}
