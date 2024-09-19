package com.goofy.tunabank.v1.exception.Transaction;

public class InsufficientBalanceException extends RuntimeException {

  public InsufficientBalanceException(double balance, double amount) {
    super(String.format("잔액이 부족합니다. 잔액: %f, 출금 신청 금액: %f", balance, amount));
  }
}
