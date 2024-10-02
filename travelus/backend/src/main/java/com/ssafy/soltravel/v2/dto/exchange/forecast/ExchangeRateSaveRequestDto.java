package com.ssafy.soltravel.v2.dto.exchange.forecast;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
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
public class ExchangeRateSaveRequestDto {

  private Map<String, ExchangeRateData> currencies = new HashMap<>();

  @JsonProperty("last_updated")
  private LocalDateTime lastUpdated;

  @JsonAnySetter
  public void setCurrency(String currencyCode, ExchangeRateData exchangeRateData) {
    this.currencies.put(currencyCode, exchangeRateData);
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ExchangeRateData {

    @JsonProperty("forecast")
    private Map<String, Double> forecast;

    @JsonProperty("average_forecast")
    private Double averageForecast;

    @JsonProperty("confidence_interval")
    private Map<String, Double> confidenceInterval;

    @JsonProperty("recent_rates")
    private Map<String, Object> recentRates;
  }
}