package com.goofy.tunabank.v1.exception.Transaction;

public class InvalidMerchantIdException extends RuntimeException{
  public InvalidMerchantIdException(Long id){
    super(String.format("유효하지 않은 가맹점 Id : %d", id));
  }
}
