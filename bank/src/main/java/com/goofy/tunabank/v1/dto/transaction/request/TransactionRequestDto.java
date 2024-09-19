package com.goofy.tunabank.v1.dto.transaction.request;

import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import lombok.Data;

@Data
public class TransactionRequestDto {
  
  //헤더
  private TransactionHeader header;
  
  //계좌 Id
  private Long accountId;

  //통화 Id
  private int currencyId;

  //거래 타입
  private TransactionType transactionType;

  //금액
  private double transactionBalance;

  //메모
  private String transactionSummary;
}
