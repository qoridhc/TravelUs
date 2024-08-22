package com.ssafy.soltravel.exception;

import com.ssafy.soltravel.dto.user.UserSearchRequestDto;

public class UserNotFoundException extends RuntimeException {

  private final Long userId;

  public UserNotFoundException(UserSearchRequestDto dto) {
    super(String.format("User Not Found about: %s", dto.toString()));
    this.userId = 0L;
  }

  public UserNotFoundException(Long userId) {
    super(String.format("User Not Found: %d", userId));
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }
}
