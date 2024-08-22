package com.ssafy.soltravel.dto.account;

import com.ssafy.soltravel.dto.currency.CurrencyDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAccountDto {

    private int bankCode;

    private String accountNo;

    private CurrencyDto currency;
}
