package com.goofy.tunabank.v1.dto.exchange;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeAmountRequestDto {

  private double amount;
  private double exchangeRate;
}
