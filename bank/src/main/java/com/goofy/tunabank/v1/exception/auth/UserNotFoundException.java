package com.goofy.tunabank.v1.exception.auth;

import com.goofy.tunabank.v1.dto.auth.ApiKeyIssueRequestDto;
import com.goofy.tunabank.v1.exception.CustomException;

public class UserNotFoundException extends CustomException {

  private static final String EMAIL_NOT_REGISTER_MESSAGE = "This email is not registered";

  private static final String DEFAULT_CODE = "EMAIL_NOT_REGISTERED";
  private static final int DEFAULT_STATUS = 401;

  public UserNotFoundException(String message) {
    super(message, "INTERNAL_SERVER_ERROR", 500, message);
  }

  public UserNotFoundException(ApiKeyIssueRequestDto request) {
    super(
        EMAIL_NOT_REGISTER_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", EMAIL_NOT_REGISTER_MESSAGE, request.getManagerId())
    );
  }
}
