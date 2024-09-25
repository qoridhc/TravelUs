package com.ssafy.soltravel.v2.dto.group.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupCodeGenerateResponseDto {

  private String groupCode;
}
