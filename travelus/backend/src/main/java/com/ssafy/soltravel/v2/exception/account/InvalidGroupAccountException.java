package com.ssafy.soltravel.v2.exception.account;

public class InvalidGroupAccountException extends RuntimeException {

  public InvalidGroupAccountException() {
    super(String.format("모임 생성 계좌는 그룹 계좌여야합니다."));
  }
}
