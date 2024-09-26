package com.ssafy.soltravel.v2.service.exchange;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.ExchangeRateForecast;
import com.ssafy.soltravel.v2.dto.exchange.forecast.ExchangeRateSaveRequestDto;
import com.ssafy.soltravel.v2.repository.ExchangeRateForecastRepository;
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

      // 예측 하지 않은 통화 코드는 제외
      Map<String, String> forecast = request.getCurrencies().get(currency).getForecast();
      if(forecast == null) continue;

      // 기준일, 통화코드 저장
      List<ExchangeRateForecast> list = new ArrayList<>();
      CurrencyType currencyType = exchangeService.getCurrencyType(currency);
      LocalDate baseDate = LocalDate.now();

      // 예측 수행한 미래기준(기준일, 차일, 환율, 통화코드)를 저장
      forecast.forEach((date, rate) -> {
        LocalDate predDate = LocalDate.parse(date);
        Double predRate = Double.valueOf(rate);

        long daysDiff = ChronoUnit.DAYS.between(baseDate, predDate);
        ExchangeRateForecast pred = ExchangeRateForecast.create(
            baseDate,
            daysDiff,
            currencyType,
            predRate
        );
        list.add(pred);
      });

      exchangeRateForecastRepository.save(list);
      length += list.size();
    }
    return length;
  }
}
