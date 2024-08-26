package com.ssafy.soltravel.dto.participants.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AddParticipantRequestDto {

    @Schema(description = "참가자의 ID", example = "1")
    Long participantId;
}
