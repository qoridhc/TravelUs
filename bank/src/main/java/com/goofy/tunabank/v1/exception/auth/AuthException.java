package com.goofy.tunabank.v1.exception.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class AuthException extends RuntimeException {

  private String message;
  private String apiKey;
  private String code;
  private int status;

  public AuthException(String message, String apiKey, String code, int status) {
    super(message);
    this.message = message;
    this.apiKey = apiKey;
    this.code = code;
    this.status = status;
  }
}

