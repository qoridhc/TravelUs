package com.ssafy.soltravel.dto.transaction.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransactionRequestDto {

    @Schema(description = "거래 금액", example = "5000")
    Long transactionBalance;

    @Schema(description = "거래 요약", example = "월급 입금")
    String transactionSummary;

}
