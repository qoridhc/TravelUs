package com.ssafy.soltravel.dto.exchange;

import lombok.Data;

@Data
public class ExchangeCurrencyDto {

  private String amount;
  private String exchangeRate;
  private String currency;
  private String currencyName;
}
