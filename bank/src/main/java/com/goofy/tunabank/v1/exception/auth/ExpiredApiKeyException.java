package com.goofy.tunabank.v1.exception.auth;

import java.time.LocalDateTime;

public class ExpiredApiKeyException extends AuthException {
  private static final String MESSAGE = "Api Key expired at: ";

  public ExpiredApiKeyException(String apiKey, LocalDateTime expireAt) {
    super(
        String.format("%s: %s", MESSAGE, expireAt.toLocalDate()),
        apiKey,
        "UNAUTHORIZED",
        401,
        String.format("%s: %s", MESSAGE, expireAt.toLocalDate())
    );
  }
}
