package com.ssafy.soltravel.v2.dto.transaction.request;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.Enum.TransferType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MoneyBoxTransferRequestDto {

  @Schema(description = "이체 타입", example = "M")
  TransferType transferType;

  @Schema(description = "출금 계좌 번호", example = "002-45579486-209")
  String accountNo;

  @Schema(description = "환전 신청 moneyBox의 통화Code", example = "KRW")
  CurrencyType sourceCurrencyCode;

  @Schema(description = "환전 금액이 입금될 moneyBox의 통화Code", example = "USD")
  CurrencyType targetCurrencyCode;

  @Schema(description = "환전 신청 금액", example = "250000")
  String transactionBalance;

  public static MoneyBoxTransferRequestDto create(
      TransferType transferType,
      String accountNo,
      CurrencyType sourceCurrencyCode,
      CurrencyType targetCurrencyCode,
      String transactionBalance) {

    MoneyBoxTransferRequestDto requestDto = new MoneyBoxTransferRequestDto();
    requestDto.setTransferType(transferType);
    requestDto.setAccountNo(accountNo);
    requestDto.setSourceCurrencyCode(sourceCurrencyCode);
    requestDto.setTargetCurrencyCode(targetCurrencyCode);
    requestDto.setTransactionBalance(transactionBalance);

    return requestDto;
  }
}
