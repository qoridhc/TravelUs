package com.ssafy.soltravel.v2.dto.group.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateParticipantRequestDto {

    @Schema(description = "참여자의 유저 ID", example = "1")
    private Long userId;

    @Schema(description = "모임 ID", example = "1")
    private Long groupId;

    @Schema(description = "그룹장 여부 (true: 그룹장, false: 일반 참여자)", example = "true")
    private boolean isMaster;

    @Schema(description = "참여자의 개인 계좌 번호", example = "002-6157654-209")
    private String personalAccountNo;

    @Schema(description = "개인 계좌의 비밀번호", example = "password123!")
    private String accountPassword;

}
