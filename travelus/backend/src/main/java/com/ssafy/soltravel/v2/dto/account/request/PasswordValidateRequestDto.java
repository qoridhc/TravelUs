package com.ssafy.soltravel.v2.dto.account.request;

import lombok.Data;

@Data
public class PasswordValidateRequestDto {

  String accountNo;
  String accountPassword;
}
