package com.ssafy.soltravel.dto.settlement;

import lombok.Data;

@Data
public class SettlementResponseDto {

  private long accountId;
  private String accountNo;
  private long userId;
  private long amountPerPerson;
  private String message;
}
