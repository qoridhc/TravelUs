package com.ssafy.soltravel.dto.account.request;

import com.ssafy.soltravel.domain.Enum.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateAccountRequestDto {

    @Schema(description = "계좌 유형 (개인 : INDIVIDUAL, 그룹 : GROUP)", example = "GROUP")
    private AccountType accountType;

    @Schema(description = "통화 코드 (예: KRW, USD)", example = "USD")
    private String currencyCode;
}
