package com.ssafy.soltravel.dto.exchange;

import lombok.Data;

@Data
public class ExchangeCurrencyDto {

  private Double amount;
  private Double exchangeRate;
  private String currency;
  private String currencyName;
}
