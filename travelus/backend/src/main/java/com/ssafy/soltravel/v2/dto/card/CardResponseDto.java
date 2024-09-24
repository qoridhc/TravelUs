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
public class CardResponseDto {

  private String cardNo;
  private String cvc;
  private String cardUniqueNo;
  private String cardIssuerName;
  private String cardName;
  private String cardDescription;
  private String cardExpiryDate;
  private String withdrawalAccountNo;

}
