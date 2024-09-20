package com.goofy.tunabank.v1.dto.transaction.request;

import lombok.Data;

@Data
public class TransferRequestDto {

  //헤더
  private TransactionHeader header;

  //출금 계좌 Id
  private Long withdrawalAccountId;

  //입금 계좌 Id
  private Long depositAccountId;

  //금액
  private double transactionBalance;

  //출금 메모
  private String withdrawalTransactionSummary;

  //입금 메모
  private String depositTransactionSummary;
}
