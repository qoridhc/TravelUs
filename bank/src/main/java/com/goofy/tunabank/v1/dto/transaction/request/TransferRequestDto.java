package com.goofy.tunabank.v1.dto.transaction.request;

import com.goofy.tunabank.v1.domain.Enum.AccountType;
import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import lombok.Data;

@Data
public class TransferRequestDto {

  //출금 계좌 Id
  private Long withdrawalAccountId;

  //출금 계좌 Type
  private AccountType withdrawalAccountType;

  //입금 계좌 Id
  private Long depositAccountId;

  //입금 계좌 Type
  private AccountType depositAccountType;

  //금액
  private double transactionBalance;

  //메모
  private String transactionSummary;
}
