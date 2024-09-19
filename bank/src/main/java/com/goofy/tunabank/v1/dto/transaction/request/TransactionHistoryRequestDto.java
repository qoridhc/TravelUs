package com.goofy.tunabank.v1.dto.transaction.request;

import com.goofy.tunabank.v1.domain.Enum.AccountType;
import com.goofy.tunabank.v1.domain.Enum.OrderByType;
import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import java.time.LocalDate;
import lombok.Data;

@Data
public class TransactionHistoryRequestDto {

  private Long accountId;

  private AccountType accountType;

  private LocalDate startDate;

  private LocalDate endDate;

  private TransactionType transactionType;

  private OrderByType orderByType;
}
