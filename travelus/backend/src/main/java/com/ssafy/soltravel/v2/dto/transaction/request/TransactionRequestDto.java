package com.ssafy.soltravel.v2.dto.transaction.request;

import com.ssafy.soltravel.v2.domain.Enum.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransactionRequestDto {

    @Schema(description = "계좌 Id", example = "1")
    Long accountId;

    @Schema(description = "머니박스 통화", example = "1")
    int currencyId;

    @Schema(description = "거래 타입", example = "D")
    TransactionType transactionType;

    @Schema(description = "거래 금액", example = "5000")
    Long transactionBalance;

    @Schema(description = "거래 요약", example = "월급 입금")
    String transactionSummary;
}
