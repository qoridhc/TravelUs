package com.goofy.tunabank.v1.exception.Transaction;

public class InvalidWithdrawalAmount extends Exception{
  public InvalidWithdrawalAmount(double amount) {
    super(String.format("출금 금액는 0원을 초과해야합니다. 출금 신청 금액 : %f", amount));
  }

}
