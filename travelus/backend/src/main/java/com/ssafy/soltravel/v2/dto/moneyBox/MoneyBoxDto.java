package com.ssafy.soltravel.v2.dto.moneyBox;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyBoxDto {

    private Long moneyBoxId;

    private double balance;

    private CurrencyType currencyCode;

}
