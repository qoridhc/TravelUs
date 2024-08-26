package com.ssafy.soltravel.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserLoginResponseDto {

    @Schema(description = "사용자의 ID", example = "1")
    private Long userId;

    @Schema(description = "인증에 사용되는 액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "새로운 액세스 토큰을 발급받기 위한 리프레시 토큰", example = "dGhpc0lzQVJFRGVtb1JlZnJlc2hUb2tlbg==")
    private String refreshToken;

}
