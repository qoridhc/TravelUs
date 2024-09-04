package com.ssafy.soltravel.exception;

import com.ssafy.soltravel.dto.user.UserSearchRequestDto;
import lombok.Getter;

@Getter
public class ForeignAccountNotFoundException extends RuntimeException {

  private String accountNo;
  public ForeignAccountNotFoundException(String accountNo) {
    super(String.format("Foreign Account Not Found: %s", accountNo));
    this.accountNo = accountNo;
  }
}
