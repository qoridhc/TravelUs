package com.ssafy.soltravel.v2.dto.transaction.request;

import com.ssafy.soltravel.v2.domain.Enum.TransferType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransferRequestDto {

    @Schema(description = "이체 타입", example = "G")
    TransferType transferType;

    @Schema(description = "출금 계좌번호", example = "1")
    String withdrawalAccountNo;

    @Schema(description = "입금 계좌번호", example = "2")
    String depositAccountNo;

    @Schema(description = "거래 금액", example = "5000")
    Long transactionBalance;

    @Schema(description = "출금 거래 요약", example = "이체 출금 테스트")
    String withdrawalTransactionSummary;

    @Schema(description = "입금 거래 요약", example = "이체 입금 테스트")
    private String depositTransactionSummary;
}
