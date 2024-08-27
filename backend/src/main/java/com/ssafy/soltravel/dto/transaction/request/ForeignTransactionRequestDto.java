package com.ssafy.soltravel.dto.transaction.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ForeignTransactionRequestDto {

  @Schema(description = "입금 금액", example = "1500.50")
  private Double transactionBalance;

  @Schema(description = "거래 요약", example = "환전 입금")
  private String transactionSummary;
}
