package com.ssafy.soltravel.dto.exchange;

import lombok.Data;

@Data
public class AccountInfoDto {

  private String accountNo;
  private Long accountId;
  private Double amount;
  private Double balance;
}
