package com.ssafy.soltravel.v2.dto.exchange.forecast;

import com.fasterxml.jackson.annotation.JsonAnySetter;
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
public class ExchangeRateSaveRequestDto {

  private Map<String, ForecastDto> currencies = new HashMap<>();

  @JsonProperty("last_updated")
  private String lastUpdated;

  @JsonAnySetter
  public void setCurrency(String currencyCode, ForecastDto forecastDto) {
    this.currencies.put(currencyCode, forecastDto);
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ForecastDto {
    private Map<String, String> forecast;
  }
}