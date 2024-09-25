package com.ssafy.soltravel.v2.dto.group;

import lombok.Data;

@Data
public class ParticipantDto {

    private Long participantId;

    private Long userId;

    private boolean isMaster;

    private String personalAccountNo;

    private String createdAt;

    private String updatedAt;
}
