package com.ssafy.soltravel.v2.dto.group.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateGroupRequestDto {

    @Schema(description = "모임의 계좌 번호", example = "002-4561621-209")
    private String groupAccountNo;

    @Schema(description = "모임 계좌의 비밀번호", example = "password123!")
    private String accountPassword;

    @Schema(description = "모임의 이름", example = "TravalUs 여행 모임")
    private String groupName;

    @Schema(description = "모임의 아이콘", example = "airPlane")
    private String icon;

    @Schema(description = "여행 시작 날짜", example = "2024-01-01")
    private String travelStartDate;

    @Schema(description = "여행 종료 날짜", example = "2024-01-07")
    private String travelEndDate;
}
