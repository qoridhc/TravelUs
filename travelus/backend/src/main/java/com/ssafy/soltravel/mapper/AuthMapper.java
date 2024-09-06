package com.ssafy.soltravel.mapper;

import com.ssafy.soltravel.dto.auth.AuthReissueResponseDto;
import com.ssafy.soltravel.dto.user.UserLoginResponseDto;

public class AuthMapper {

  public static AuthReissueResponseDto convertLoginToReissueDto(UserLoginResponseDto login){
    return AuthReissueResponseDto.builder()
        .accessToken(login.getAccessToken())
        .refreshToken(login.getRefreshToken())
        .build();
  }
}
