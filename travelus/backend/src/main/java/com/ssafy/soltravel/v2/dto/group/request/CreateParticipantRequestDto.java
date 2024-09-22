package com.ssafy.soltravel.v2.dto.group.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateParticipantRequestDto {

    private Long userId;

    private Long groupId;

    private boolean isMaster;

    private String personalAccountNo;

    private String accountPassword;

}
