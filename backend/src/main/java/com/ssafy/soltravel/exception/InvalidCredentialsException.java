package com.ssafy.soltravel.exception;

import lombok.Getter;

@Getter
public class InvalidCredentialsException extends RuntimeException {

  private String email;

  public InvalidCredentialsException(String email) {
    super(String.format("Invalid credentials for email: %s", email));
  }
}
