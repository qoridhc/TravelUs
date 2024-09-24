package com.ssafy.soltravel.v2.dto.account.request;

import com.ssafy.soltravel.v1.domain.Enum.AccountType;
import com.ssafy.soltravel.v2.dto.group.request.CreateGroupRequestDto;
import com.ssafy.soltravel.v2.dto.group.request.CreateParticipantRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDto {

    @Schema(description = "유저 아이디", example = "1")
    private Long userId;

    @Schema(description = "계좌 유형 (개인 : I / 모임 : G)", example = "I")
    private AccountType accountType;

    @Schema(description = "계좌의 비밀번호", example = "password123!")
    private String accountPassword;

    @Schema(description = "은행 ID (은행 식별자)", example = "1")
    private int bankId;

    public static CreateAccountRequestDto createDto(Long userId, AccountType accountType, String accountPassword, int bankId) {
        return CreateAccountRequestDto.builder()
            .userId(userId)
            .accountType(accountType)
            .accountPassword(accountPassword)
            .bankId(bankId)
            .build();
    }

}
