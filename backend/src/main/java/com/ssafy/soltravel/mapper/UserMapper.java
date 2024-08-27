package com.ssafy.soltravel.mapper;

import com.ssafy.soltravel.domain.User;
import com.ssafy.soltravel.dto.user.UserCreateRequestDto;
import com.ssafy.soltravel.dto.user.UserProfileResponseDto;
import com.ssafy.soltravel.dto.user.UserSearchResponseDto;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    // 응애
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

    // 회원가입 요청 DTO를 유저 엔티티로 변환
    public static User convertCreateDtoToUserWithUserKey(
        UserCreateRequestDto dto, String profileImageUrl, String userKey
    ) {
        return User.createUser(
            dto.getName(),
            dto.getPassword(),
            dto.getEmail(),
            dto.getPhone(),
            dto.getAddress(),
            dto.getBirth(),
            profileImageUrl,
            userKey
        );
    }

    // 회원가입 요청 DTO를 유저 엔티티로 변환(신한 API X)
    public static User convertCreateDtoToUserWithUserKey(UserCreateRequestDto dto, String profileImageUrl) {
        return User.createUser(
            dto.getName(),
            dto.getPassword(),
            dto.getEmail(),
            dto.getPhone(),
            dto.getAddress(),
            dto.getBirth(),
            profileImageUrl
        );
    }
}
