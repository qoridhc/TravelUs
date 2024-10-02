package com.ssafy.soltravel.v2.service.exchange;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.ExchangeRateForecast;
import com.ssafy.soltravel.v2.dto.exchange.forecast.ExchangeRateSaveRequestDto;
import com.ssafy.soltravel.v2.repository.ExchangeRateForecastRepository;
import com.ssafy.soltravel.v2.util.LogUtil;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExchangeRateForecastService {

  private final ExchangeService exchangeService;
  private final ExchangeRateForecastRepository exchangeRateForecastRepository;

  //-----------------------------환율 예측-----------------------------
  public int saveExchangeRate(ExchangeRateSaveRequestDto request) {
    int length = 0;

    for (String currency : request.getCurrencies().keySet()) {

      // 통화코드 조회
      CurrencyType currencyType = exchangeService.getCurrencyType(currency);
      ExchangeRateSaveRequestDto.ExchangeRateData data = request.getCurrencies().get(currency);

      LogUtil.info("통화코드", currencyType);

      // 과거 저장
      Map<String, Object> recentRates = data.getRecentRates();
      length += saveExchangeRateHistory(recentRates, currencyType);

      // 예측값 저장
      Map<String, Double> forecast = data.getForecast();
      length += saveExchangeRatePred(forecast, currencyType);
    }
    return length;
  }


  private int saveExchangeRatePred(Map<String, Double> forecast, CurrencyType currencyType) {
    if(forecast == null) return 0;
    List<ExchangeRateForecast> list = new ArrayList<>();

    //date별 rate 저장
    forecast.forEach((date, rate) -> {
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

  private int saveExchangeRateHistory(Map<String, Object> recentRates, CurrencyType currencyType) {

    // "3_months" 키를 사용하여 해당 환율 데이터를 직접 가져옴
    Map<String, Double> month3 = (Map<String, Double>) recentRates.get("3_months");
    List<ExchangeRateForecast> list = new ArrayList<>();
    if(month3 == null) return 0;

    //date별 rate 저장
    for (Map.Entry<String, Double> rateEntry : month3.entrySet()) {
      ExchangeRateForecast pred = ExchangeRateForecast.create(
          LocalDate.parse(rateEntry.getKey()),
          currencyType,
          Double.valueOf(rateEntry.getValue())
      );
      list.add(pred);
    }
    exchangeRateForecastRepository.save(list);
    return list.size();
  }

}
