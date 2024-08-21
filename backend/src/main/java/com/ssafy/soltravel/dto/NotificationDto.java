package com.ssafy.soltravel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationDto {

  private String accountNo;
  private float exchangeRate;
  private String message;
}
