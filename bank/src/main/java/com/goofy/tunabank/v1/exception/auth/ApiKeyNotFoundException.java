package com.goofy.tunabank.v1.exception.auth;

public class ApiKeyNotFoundException extends AuthException {
  private static final String MESSAGE = "Api Key does not exist";

  public ApiKeyNotFoundException(String apiKey) {
    super(
        MESSAGE,
        apiKey,
        "UNAUTHORIZED",
        401,
        String.format("%s: %s", MESSAGE, apiKey)
    );
  }
}
