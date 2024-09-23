package com.ssafy.soltravel.v2.dto.account.request;

import com.ssafy.soltravel.v2.domain.Enum.AccountType;
import com.ssafy.soltravel.v2.dto.user.EmailValidationDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDto {

    @Schema(description = "계좌 유형 (개인 : I / 모임 : G)", example = "I")
    private String accountType;

    @Schema(description = "계좌의 비밀번호", example = "password123!")
    private String accountPassword;

    @Schema(description = "은행 ID (은행 식별자)", example = "1")
    private int bankId;
}
