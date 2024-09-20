package com.goofy.tunabank.v1.dto.transaction.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class TransactionResponseDto {

  //거래 기록 id
  private Long transactionHistoryId;

  //거래 종류
  private TransactionType transactionType;

  //상대 계좌 번호
  private String accountNo;

  //거래 일시
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime transactionAt;

  //금액
  private double amount;

  //잔액
  private double balance;

  //메모
  private String summary;
}
