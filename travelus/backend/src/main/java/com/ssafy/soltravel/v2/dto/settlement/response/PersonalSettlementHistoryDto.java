package com.ssafy.soltravel.v2.dto.settlement.response;

import com.ssafy.soltravel.v2.domain.Enum.SettlementStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalSettlementHistoryDto {

  private Long personalSettlementId;

  private double amount;

  private double remainingAmount;

  private SettlementStatus isSettled;
}
