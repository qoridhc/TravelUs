package com.ssafy.soltravel.v2.service.exchange;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.ExchangeRate;
import com.ssafy.soltravel.v2.dto.exchange.forecast.ExchangeRateForecastResponseDto;
import com.ssafy.soltravel.v2.dto.exchange.forecast.ExchangeRateForecastResponseDto.ExchangeRateData;
import com.ssafy.soltravel.v2.dto.exchange.forecast.ExchangeRateSaveRequestDto;
import com.ssafy.soltravel.v2.exception.exchange.CurrencyCodeInvalidException;
import com.ssafy.soltravel.v2.repository.ExchangeRateForecastRepository;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

  private final static int DEFAULT_UPDATE_PERIOD = 7;


  //-----------------------------과거 환율 전체 저장-----------------------------
  public int saveAllHistories(ExchangeRateSaveRequestDto request) {
    int length = 0;

    for (String currency : request.getCurrencies().keySet()) {
      CurrencyType currencyType = exchangeService.getCurrencyType(currency);
      ExchangeRateSaveRequestDto.ExchangeRateData data = request.getCurrencies().get(currency);

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
      ExchangeRateSaveRequestDto.ExchangeRateData data = request.getCurrencies().get(currency);

      // 기간별 데이터 저장 메서드 호출
      Map<String, Double> recentRates = (Map<String, Double>) data.getRecentRates().get("3_months");
      length += updateExchangeRateByPeriod(recentRates, currencyType, DEFAULT_UPDATE_PERIOD);
    }

    return length;
  }


  // 기간별 환율 저장
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

  // 기간별 환율 수정
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

  // 데이터 기간으로 파싱
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













  // -----------------------------환율 기간 조회-----------------------------
  public Map<String, ExchangeRateData> findRatesByCurrencyCode(String currencyCode) {
    CurrencyType cType = exchangeService.getCurrencyType(currencyCode);

    // 최근 3개월치 환율 데이터 조회
    List<ExchangeRate> recentRates_3 = exchangeRateForecastRepository.findByPeriodAndCurrency(
        LocalDate.now().minusMonths(3),
        LocalDate.now(),
        cType
    ).orElseThrow(
        () -> new CurrencyCodeInvalidException(currencyCode)
    );

    // 2주치 예측 환율 데이터 조회
    List<ExchangeRate> forecast = exchangeRateForecastRepository.findByPeriodAndCurrency(
        LocalDate.now(),
        LocalDate.now().plusWeeks(2),
        cType
    ).orElse(
        null
    );

    return toDto(
        forecast,
        recentRates_3,
        currencyCode
    );
  }

  // -----------------------------전체 환율 기간 조회-----------------------------
  public Map<String, ExchangeRateData> findAllRates() {
    Map<String, ExchangeRateData> allExchangeRates = new LinkedHashMap<>();
    for(CurrencyType ctype: CurrencyType.values()) {
      if(ctype == CurrencyType.KRW) continue;
      Map<String, ExchangeRateData> exchangeRate = findRatesByCurrencyCode(String.valueOf(ctype));
      allExchangeRates.putAll(exchangeRate);
    }

    return allExchangeRates;
  }



  //데이터(도메인) -> DTO
  private Map<String, ExchangeRateData> toDto(
      List<ExchangeRate> forecast,
      List<ExchangeRate> recent,
      String currencyCode
  ){

    // 초기화
    ExchangeRateForecastResponseDto responseDto = new ExchangeRateForecastResponseDto();
    ExchangeRateForecastResponseDto.ExchangeRateData exchangeRateData = new ExchangeRateForecastResponseDto.ExchangeRateData();

    // 예측 데이터를 Map으로 변환
    Map<String, Double> forecastMap = new LinkedHashMap<>();
    if(forecast != null) {
      for (ExchangeRate rate : forecast) {
        forecastMap.put(rate.getDate().toString(), rate.getRate());
      }
    }

    // 최근 3개월치 데이터를 Map으로 변환
    Map<String, Map<String, Double>> recentRatesMap = new HashMap<>();
    Map<String, Double> recentRatesValues = new LinkedHashMap<>();
    for (ExchangeRate rate : recent) {
      recentRatesValues.put(rate.getDate().toString(), rate.getRate());
    }
    recentRatesMap.put("3_months", recentRatesValues);

    // DTO에 추가
    exchangeRateData.setForecast(forecastMap);
    exchangeRateData.setRecentRates(recentRatesMap);
    responseDto.getCurrencies().put(currencyCode, exchangeRateData);
    return responseDto.getCurrencies();
  }

}
