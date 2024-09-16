package com.goofy.tunabank.v1.exception.Transaction;

public class TransactionHistoryNotFoundException extends RuntimeException {
  public TransactionHistoryNotFoundException() {
    super(String.format("거래 기록이 없습니다."));
  }
}
