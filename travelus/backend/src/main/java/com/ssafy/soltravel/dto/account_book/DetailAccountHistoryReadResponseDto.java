package com.ssafy.soltravel.dto.account_book;

import com.ssafy.soltravel.domain.Enum.CashTransactionType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailAccountHistoryReadResponseDto {

  private String amount;
  private String transactionType;
  private LocalDateTime transactionAt;
  private String balance;
  private String store;
}
