package com.goofy.tunabank.v1.dto.transaction.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goofy.tunabank.v1.common.Header;
import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.domain.Enum.TransferType;
import lombok.Data;

@Data
public class TransferMBRequestDto {

  //헤더
  @JsonProperty("Header")
  private Header header;

  //이체 종류
  private TransferType transferType;

  //계좌번호
  private String accountNo;

  //계좌 비밀번호
  private String accountPassword;

  //변경 전 통화
  private CurrencyType sourceCurrencyCode;

  //변경 후 통화
  private CurrencyType targetCurrencyCode;

  //신청 금액
  private double transactionBalance;
}
