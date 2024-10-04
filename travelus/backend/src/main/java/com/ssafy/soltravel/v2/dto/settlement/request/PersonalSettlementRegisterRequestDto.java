package com.ssafy.soltravel.v2.dto.settlement.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Data
public class PersonalSettlementRegisterRequestDto {

  @Schema(description = "참여자별 정산금액")
  private List<SettlementParticipantRequestDto> participants;
}
