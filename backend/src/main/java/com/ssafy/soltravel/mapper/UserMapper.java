package com.ssafy.soltravel.mapper;

import com.ssafy.soltravel.domain.User;
import com.ssafy.soltravel.dto.user.UserProfileResponseDto;

public class UserMapper {


    public static UserProfileResponseDto convertUserToProfileResponseDto(User user) {
        return UserProfileResponseDto.builder()
            .userId(user.getUserId())
            .username(user.getName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .address(user.getAddress())
            .birth(user.getBirth())
            .registerAt(user.getRegisterAt())
            .build();
    }

}
