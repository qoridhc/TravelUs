package com.ssafy.soltravel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationDto {

  private long accountId;
  private String accountNo;
  private String exchangeRate;
  private String message;
}
