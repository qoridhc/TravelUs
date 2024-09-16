package com.goofy.tunabank.v1.exception.auth;

import lombok.Data;

@Data
public class InvalidApiKeyException extends AuthException {
  public InvalidApiKeyException(String apiKey) {
    super(
        "Invalid Api Key",
        apiKey,
        "UNAUTHORIZED",
        401
    );
  }
}
