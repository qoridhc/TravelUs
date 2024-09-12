package com.goofy.tunabank.dto.exchange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ExchangeRateCacheDTO {

  String CurrencyCode;
  double ExchangeRate;
}
