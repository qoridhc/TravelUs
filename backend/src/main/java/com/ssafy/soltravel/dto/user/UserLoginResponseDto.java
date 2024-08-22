package com.ssafy.soltravel.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserLoginResponseDto {

  private Long userId;
  private String accessToken;
  private String refreshToken;
}
