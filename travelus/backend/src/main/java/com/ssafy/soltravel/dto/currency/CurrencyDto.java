package com.ssafy.soltravel.dto.currency;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyDto {
	private String currencyCode;
	private String currencyName;
}