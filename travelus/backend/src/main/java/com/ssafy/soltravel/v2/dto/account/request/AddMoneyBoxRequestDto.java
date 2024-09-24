package com.ssafy.soltravel.v2.dto.account.request;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AddMoneyBoxRequestDto {

    @Schema(description = "계좌를 조회하는 유저의 ID", example = "1")
    private Long userId;

    @Schema(description = "조회할 계좌 번호", example = "002-4262233-209")
    private String accountNo;

    @Schema(description = "통화 코드 ( KRW / USD / JPY / EUR / CNY )", example = "USD")
    private CurrencyType currencyCode;

}
