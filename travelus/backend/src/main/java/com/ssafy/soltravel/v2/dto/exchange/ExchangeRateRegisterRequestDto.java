package com.ssafy.soltravel.v2.dto.exchange;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ExchangeRateRegisterRequestDto {

  @Schema(description = "계좌 아이디", example = "1")
  private long accountId;

  @Schema(description = "환전할 통화 코드", example = "USD")
  private CurrencyType currencyCode;

  @Schema(description = "환전할 금액", example = "130000")
  private double transactionBalance;

  @Schema(description = "목표 환율", example = "1333.40")
  private float targetRate;
}
