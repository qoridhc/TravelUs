package com.ssafy.soltravel.v2.dto.card;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.soltravel.v2.common.BankHeader;
import com.ssafy.soltravel.v2.common.Header;
import io.swagger.v3.oas.annotations.media.Schema;
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
  @Schema(description = "카드 고유 번호, 8자리 문자열", example = "DC5O2YKQ")
  private String cardUniqueNo;         // 카드 고유 번호

  @NotEmpty
  @Length(min = 17, max = 17)
  @Schema(description = "출금 계좌 번호, 17자리 문자열", example = "12345678901234567")
  private String withdrawalAccountNo;  // 출금 계좌 번호

  @Null
  @JsonProperty("Header")
  @Schema(hidden = true)
  private BankHeader header;
}
