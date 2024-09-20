package com.goofy.tunabank.v1.dto.transaction.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goofy.tunabank.v1.common.Header;
import com.goofy.tunabank.v1.domain.Currency;
import lombok.Data;

@Data
public class TransferBetweenMoneyBoxesRequestDto {

  //헤더
  @JsonProperty("Header")
  private Header header;

  //통장 id
  private Long accountId;

  //변경 전 통화
  private Currency sourceCurrency;

  //변경 후 통화
  private Currency targetCurrency;

  //신청 금액
  private double amount;
}
