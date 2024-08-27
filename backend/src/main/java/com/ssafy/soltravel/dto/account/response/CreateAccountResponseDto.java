package com.ssafy.soltravel.dto.account.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.soltravel.dto.account.CreateAccountDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "계좌 생성 응답 DTO")
@JsonInclude(JsonInclude.Include.NON_NULL) // null인거 제외하고 직렬화
public class CreateAccountResponseDto {

    @Schema(
        description = "일반 계좌 정보",
        implementation = CreateAccountDto.class,
        example = "{\"accountNo\": \"0881234567890\", \"balance\": 10000, \"currency\": \"KRW\"}"
    )
    private CreateAccountDto generalAccount;

    @Schema(
        description = "외화 계좌 정보",
        implementation = CreateAccountDto.class,
        example = "{\"accountNo\": \"0889876543210\", \"balance\": 5000, \"currency\": \"USD\"}"
    )
    private CreateAccountDto foreignAccount;

}