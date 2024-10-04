package com.ssafy.soltravel.v2.dto.settlement.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.soltravel.v2.domain.Enum.SettlementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PersonalSettlementDto {

  @Schema(description = "개별 지출 정산 내역 모임 id",example = "1")
  private long groupId;

  @Schema(description = "개별 지출 정산 내역 모임 이름",example = "가자")
  private String groupName;
  
  @Schema(description = "개별 지출 정산 내역 id",example = "1")
  private Long personalSettlementId;

  @Schema(description = "개별 지출 정산 금액",example = "10000")
  private double amount;

  @Schema(description = "개별 지출 정산 남은 금액",example = "5000")
  private double remainingAmount;

  @Schema(description = "개별 지출 정산 완료 여부(COMPLETED: 정산 완료, NOT_COMPLETED: 정산 미완료)",example = "NOT_COMPLETED")
  private SettlementStatus isSettled;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(description = "개별 지출 정산 요청 시각",example = "2024-10-04'T'14:05:13")
  private LocalDateTime settlementRequestTime;
  
  //모임참여자수
}
