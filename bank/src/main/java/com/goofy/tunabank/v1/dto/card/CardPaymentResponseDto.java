package com.goofy.tunabank.v1.dto.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardPaymentResponseDto {

  private Long merchantId;
  private String merchantName;
  private String category;
  private String paymentDate;
  private String paymentTime;

  private String currencyCode;
  private Long paymentBalance;

}
