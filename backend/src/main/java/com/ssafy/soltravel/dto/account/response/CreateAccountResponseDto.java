package com.ssafy.soltravel.dto.account.response;

import com.ssafy.soltravel.dto.currency.CurrencyDto;
import lombok.Data;

@Data
public class CreateAccountResponseDto {

    private String bankCode;

    private String accountNo;

    private CurrencyDto currency;

}