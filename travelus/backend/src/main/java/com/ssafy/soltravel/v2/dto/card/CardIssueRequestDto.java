package com.ssafy.soltravel.v2.dto.card;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.soltravel.v2.common.BankHeader;
import com.ssafy.soltravel.v2.common.Header;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardIssueRequestDto {
  @NotEmpty
  @Length(min = 8, max = 8)
  private String cardUniqueNo;         // 카드 고유 번호

  @NotEmpty
  @Length(min = 17, max = 17)
  private String withdrawalAccountNo;  // 출금 계좌 번호

  @Null
  @JsonProperty("Header")
  private BankHeader header;
}
