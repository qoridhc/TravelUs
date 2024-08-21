package com.ssafy.soltravel.dto.exchange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponseDto {

    private String currency;
    private Float exchangeRate;
    private Long exchangeMin;
}
