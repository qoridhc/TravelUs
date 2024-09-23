package com.ssafy.soltravel.v2.dto.exchange;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class targetAccountDto {

  long accountId;
  double amount;
  double targetRate;
}
