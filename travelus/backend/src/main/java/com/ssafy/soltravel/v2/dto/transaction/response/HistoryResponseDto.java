package com.ssafy.soltravel.v2.dto.transaction.response;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class HistoryResponseDto {

  //거래 기록 id
  private Long transactionUniqueNo;

  //거래 종류
  private String transactionType;

  //사용처(상대방이름)
  private String payeeName;

  //거래 일시
  private String transactionDate;

  //금액
  private String transactionAmount;

  //잔액
  private String transactionBalance;

  //메모
  private String transactionSummary;

  //통화 코드
  private CurrencyType currencyCode;
}
