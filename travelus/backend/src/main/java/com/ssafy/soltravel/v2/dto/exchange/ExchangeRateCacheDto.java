package com.ssafy.soltravel.v2.dto.exchange;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeRateCacheDto {

  String CurrencyCode;
  double ExchangeRate;
  String lastUpdatedTime;
}
