package com.ssafy.soltravel.v2.dto.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.soltravel.v2.domain.Enum.AccountType;
import com.ssafy.soltravel.v2.dto.currency.CurrencyDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {

    @Schema(description = "계좌 ID", example = "1")
    private Long id;

    @Schema(description = "은행 코드", example = "088")
    private int bankCode;

    @Schema(description = "계좌 비밀번호", example = "1234")
    private String accountPassword;

    @Schema(description = "계좌 번호", example = "0889876543210")
    private String accountNo;

    @Schema(description = "계좌 유형 (예: 개인 또는 그룹)", example = "GROUP")
    private AccountType accountType;
}
