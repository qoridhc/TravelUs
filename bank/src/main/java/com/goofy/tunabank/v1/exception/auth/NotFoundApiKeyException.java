package com.goofy.tunabank.v1.exception.auth;

import lombok.Data;

@Data
public class NotFoundApiKeyException extends AuthException {
  public NotFoundApiKeyException(String apiKey) {
    super(
        "Api Key do not exist",
        apiKey,
        "UNAUTHORIZED",
        401
    );
  }
}
