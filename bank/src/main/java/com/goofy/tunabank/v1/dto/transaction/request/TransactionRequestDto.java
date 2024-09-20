package com.goofy.tunabank.v1.dto.transaction.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goofy.tunabank.v1.common.Header;
import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import lombok.Data;

@Data
public class TransactionRequestDto {
  
  //헤더
  @JsonProperty("Header")
  private Header header;
  
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
