package com.goofy.tunabank.v1.exception.auth;

import com.goofy.tunabank.v1.dto.auth.ApiKeyIssueRequestDto;
import com.goofy.tunabank.v1.exception.CustomException;

public class UserNotFoundException extends CustomException {

  private static final String DEFAULT_CODE = "UNAUTHORIZED";
  private static final int DEFAULT_STATUS = 401;
  private static final String EMAIL_MESSAGE = "This email is not registered";

  public UserNotFoundException(String message) {
    super(message, "INTERNAL_SERVER_ERROR", 500, message);
  }

  public UserNotFoundException(ApiKeyIssueRequestDto request) {
    super(
        EMAIL_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", EMAIL_MESSAGE, request.getManagerId())
    );
  }
}
