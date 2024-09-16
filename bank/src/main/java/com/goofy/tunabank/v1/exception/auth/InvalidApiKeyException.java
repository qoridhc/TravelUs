package com.goofy.tunabank.v1.exception.auth;

public class InvalidApiKeyException extends AuthException {
  private static final String MESSAGE = "Invalid Api Key";

  public InvalidApiKeyException(String apiKey) {
    super(
        MESSAGE,
        apiKey,
        "UNAUTHORIZED",
        401,
        String.format("%s: %s", MESSAGE, apiKey)
    );
  }
}
