package com.ssafy.soltravel.v2.dto.account.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquireAccountRequestDto {

    private Long userId;

    private String accountNo;

    private String accountPassword;
}
