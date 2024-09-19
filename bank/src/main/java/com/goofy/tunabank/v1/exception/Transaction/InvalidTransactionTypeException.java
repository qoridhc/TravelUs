package com.goofy.tunabank.v1.exception.Transaction;

import com.goofy.tunabank.v1.domain.Enum.TransactionType;

public class InvalidTransactionTypeException extends RuntimeException {

  public InvalidTransactionTypeException(TransactionType type) {
    super(String.format("유효하지 않은 TransactionType : %s", type));
  }
}
