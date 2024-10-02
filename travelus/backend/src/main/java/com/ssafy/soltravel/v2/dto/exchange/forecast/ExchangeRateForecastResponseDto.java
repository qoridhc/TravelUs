package com.ssafy.soltravel.v2.dto.exchange.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateForecastResponseDto {

  private Map<String, ExchangeRateData> currencies = new HashMap<>();

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ExchangeRateData {

    @JsonProperty("forecast")
    private Map<String, Double> forecast = new HashMap<>();

    @JsonProperty("recent_rates")
    private Map<String, Map<String, Double>> recentRates = new HashMap<>();
  }
}