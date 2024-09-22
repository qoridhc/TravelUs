package com.ssafy.soltravel.v2.exception.group;

public class InvalidGroupIdException extends RuntimeException {

  public InvalidGroupIdException() {
    super(String.format("존재하지 않는 그룹 Id입니다."));
  }
}
