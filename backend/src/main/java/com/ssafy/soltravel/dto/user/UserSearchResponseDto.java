package com.ssafy.soltravel.dto.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchResponseDto {

  private Long userId;
  private String name;
  private String email;
  private String phone;
  private String address;
  private LocalDate birth;
  private LocalDateTime registerAt;
  private Boolean isExit;
  private String userKey;
}
