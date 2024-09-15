package com.goofy.tunabank.v1.dto.transaction.response;

import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponseDto {

  //거래 기록 id
  private Long transactionHistoryId;

  //거래 종류
  private TransactionType transactionType;

  //계좌 번호
  private String accountNo;

  //거래 일시
  private LocalDateTime transactionAt;

  //금액
  private double amount;

  //잔액
  private double balance;
}
