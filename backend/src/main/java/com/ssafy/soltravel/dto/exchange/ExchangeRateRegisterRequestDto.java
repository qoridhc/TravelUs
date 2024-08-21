package com.ssafy.soltravel.dto.exchange;

import lombok.Data;

@Data
public class ExchangeRateRegisterRequestDto {

  private Long generalAccountId;//일반 통장 아이디
  private String currency;
  private float exchangeRate;
}
