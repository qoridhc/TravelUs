package com.ssafy.soltravel.v2.dto.card;


import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class CardIssueRequestDto {
  @NotEmpty
  @Length(min = 8, max = 8)
  private String cardUniqueNo;         // 카드 고유 번호

  @NotEmpty
  @Length(min = 17, max = 17)
  private String withdrawalAccountNo;  // 출금 계좌 번호
}
