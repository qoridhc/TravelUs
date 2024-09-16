package com.goofy.tunabank.v1.exception.auth;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ExpiredApiKeyException extends AuthException {
  public ExpiredApiKeyException(String apiKey, LocalDateTime expireAt) {
    super(
        String.format("Api Key expired at: %s", expireAt.toLocalDate()),
        apiKey,
        "UNAUTHORIZED",
        401
    );
  }
}
