package com.ssafy.soltravel.v2.dto.transaction.request;

import com.ssafy.soltravel.v2.domain.Enum.OrderByType;
import com.ssafy.soltravel.v2.domain.Enum.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryRequestDto {

    @Schema(description = "조회할 머니 박스 id", example = "2")
    private long moneyBoxId;

    @Schema(description = "조회할 거래의 시작 날짜", example = "2024-01-01")
    private String startDate;

    @Schema(description = "조회할 거래의 종료 날짜", example = "2024-09-30")
    private String endDate;

    @Schema(description = "거래 유형 (D:입금, W:출금, TD:이체입금, TW:이체출금, ED:환전입금, EW:환전출금, 안보내면 전체)", example = "A")
    private TransactionType transactionType;

    @Schema(description = "정렬 기준 (ASC: 오름차순(이전거래), DESC: 내림차순(최근거래))", example = "ASC")
    private OrderByType orderByType;
}
