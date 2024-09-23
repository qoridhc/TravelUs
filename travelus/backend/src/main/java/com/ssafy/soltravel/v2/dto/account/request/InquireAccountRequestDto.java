package com.ssafy.soltravel.v2.dto.account.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquireAccountRequestDto {

    @Schema(description = "조회할 계좌 번호", example = "002-412656-209")
    private String accountNo;

    @Schema(description = "계좌 비밀번호", example = "password123!")
    private String accountPassword;
}
