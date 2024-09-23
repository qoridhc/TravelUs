package com.ssafy.soltravel.v2.dto.group.request;

import lombok.Data;

@Data
public class CreateGroupRequestDto {

    // 모임 생성하는 유저 ID(그룹장)
    private Long userId;

    private String groupAccountNo;

    private String accountPassword;

    private String groupName;

    private String icon;

    private String travelStartDate;

    private String travelEndDate;
}
