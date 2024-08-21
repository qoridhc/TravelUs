package com.ssafy.soltravel.dto.account.response;

import com.ssafy.soltravel.dto.account.CreateAccountDto;
import com.ssafy.soltravel.dto.currency.CurrencyDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateAccountResponseDto {
    private CreateAccountDto generalAccount;

    private CreateAccountDto foreignAccount;

}