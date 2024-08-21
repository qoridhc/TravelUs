package com.ssafy.soltravel.exception;

public class UserNotFoundException extends RuntimeException {

  private final Long userId;

  public UserNotFoundException(Long userId) {
    super(String.format("User Not Found: %d", userId));
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }
}
