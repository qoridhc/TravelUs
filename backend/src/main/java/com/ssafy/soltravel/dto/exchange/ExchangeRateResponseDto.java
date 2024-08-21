package com.ssafy.soltravel.dto.exchange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponseDto {

    private String currency;
    private String exchangeRate;
    private String exchangeMin;
}
