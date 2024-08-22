package com.ssafy.soltravel.dto.user.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchRequestBody {

  private String apiKey;
  private String userId;
}
