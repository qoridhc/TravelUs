package com.ssafy.soltravel.v2.dto.transaction.request;

import com.ssafy.soltravel.v2.domain.Enum.TransferType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MoneyBoxTransferRequestDto {

  @Schema(description = "이체 타입", example = "M")
  TransferType transferType;

  @Schema(description = "출금 계좌 Id", example = "1")
  Long accountId;

  @Schema(description = "환전 신청 moneyBox의 통화Id", example = "1")
  Long sourceCurrencyId;

  @Schema(description = "환전 금액이 입금될 moneyBox의 통화Id", example = "2")
  Long targetCurrencyId;

  @Schema(description = "환전 신청 금액", example = "250000")
  String transactionBalance;
}
