package com.ssafy.soltravel.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDto {

  private String name;
  private String email;
  private String password;
  private String phone;
  private String address;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate birth;
}
