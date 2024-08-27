package com.ssafy.soltravel.dto.exchange;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ExchangeRateRegisterRequestDto {

  @Schema(description = "일반 통장 아이디", example = "1")
  private Long generalAccountId;

  @Schema(description = "환전할 통화 코드", example = "USD")
  private String currency;

  @Schema(description = "희망 환율", example = "1333.40")
  private float exchangeRate;
}
