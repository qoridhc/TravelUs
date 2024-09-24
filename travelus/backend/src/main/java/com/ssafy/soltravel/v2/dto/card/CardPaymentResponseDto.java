package com.ssafy.soltravel.v2.dto.card;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardPaymentResponseDto {

  private String merchantId;
  private String merchantName;
  private String category;
  private String paymentAt;

  private String currencyCode;
  private String paymentBalance;

}
