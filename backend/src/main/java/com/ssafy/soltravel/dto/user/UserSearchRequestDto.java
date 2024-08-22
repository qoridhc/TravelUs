package com.ssafy.soltravel.dto.user;

import lombok.Data;

@Data
public class UserSearchRequestDto {

  private Long userId;
  private String name;
  private String email;
}
