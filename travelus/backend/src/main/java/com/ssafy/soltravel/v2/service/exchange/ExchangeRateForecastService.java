package com.ssafy.soltravel.v2.service.exchange;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.ExchangeRate;
import com.ssafy.soltravel.v2.dto.exchange.forecast.common.ExchangeRateData;
import com.ssafy.soltravel.v2.dto.exchange.forecast.ExchangeRateSaveRequestDto;
import com.ssafy.soltravel.v2.repository.ExchangeRateForecastRepository;
import java.time.LocalDate;
import java.util.LinkedHashMap;
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

  private final static int DEFAULT_UPDATE_PERIOD = 7;

  //-----------------------------예측 환율 업데이트-----------------------------
  public int updatePred(){

    return 0;
  }



  //-----------------------------과거 환율 전체 저장-----------------------------
  public int saveAllHistories(ExchangeRateSaveRequestDto request) {
    int length = 0;

    for (String currency : request.getCurrencies().keySet()) {
      CurrencyType currencyType = exchangeService.getCurrencyType(currency);
      ExchangeRateData data = request.getCurrencies().get(currency);

      // 기간별 데이터 저장 메서드 호출
      Map<String, Double> recentRates = (Map<String, Double>) data.getRecentRates().get("3_months");
      length += saveExchangeRateByPeriod(recentRates, currencyType, recentRates.size());
    }

    return length;
  }

  //-----------------------------과거 환율 일부 업데이트-----------------------------
  public int updateHistories(ExchangeRateSaveRequestDto request) {
    int length = 0;

    for (String currency : request.getCurrencies().keySet()) {
      CurrencyType currencyType = exchangeService.getCurrencyType(currency);
      ExchangeRateData data = request.getCurrencies().get(currency);

      // 기간별 데이터 저장 메서드 호출
      Map<String, Double> recentRates = (Map<String, Double>) data.getRecentRates().get("3_months");
      length += updateExchangeRateByPeriod(recentRates, currencyType, DEFAULT_UPDATE_PERIOD);
    }

    return length;
  }


  // 기간별 환율 저장(공통 메서드)
  public int saveExchangeRateByPeriod(Map<String, Double> data, CurrencyType cType, int period) {
    if(period != data.size()){ //전체 데이터를 저장할경우 기간 파싱 X
      data = parseDataByPeriod(data, period);
    }

    for(String dateString : data.keySet()) {
      ExchangeRate rate = ExchangeRate.create(
          LocalDate.parse(dateString),
          cType,
          data.get(dateString)
      );
      exchangeRateForecastRepository.save(rate);
    }

    return period;
  }

  // 기간별 환율 수정 및 생성(공통 메서드)
  public int updateExchangeRateByPeriod(Map<String, Double> data, CurrencyType cType, int period) {
    if(period != data.size()){
      data = parseDataByPeriod(data, period);
    }

    for(String dateString : data.keySet()) {
      LocalDate date = LocalDate.parse(dateString);
      Double exchangeRateValue = data.get(dateString);

      ExchangeRate rate = exchangeRateForecastRepository.findByDateAndCurrency(date, cType).orElse(
          null
      );

      if(rate != null) {
        rate.updateRate(exchangeRateValue);
      }
      else{
        rate = ExchangeRate.create(
            date,
            cType,
            exchangeRateValue
        );
        exchangeRateForecastRepository.save(rate);
      }
    }

    return period;
  }

  // 데이터 기간으로 파싱(공통 메서드)
  public Map<String, Double> parseDataByPeriod(Map<String, Double> data, int period) {
    Map<String, Double> parsedData = new LinkedHashMap<>();
    for(int i = period; i > 0; i--) {
      LocalDate date = LocalDate.now().minusDays(i);
      if(!data.containsKey(date.toString())) continue;

      Double rate = data.get(date.toString());
      parsedData.put(date.toString(), rate);
    }
    return parsedData;
  }

}
