package com.goofy.tunabank.v1.dto.account.request;

import com.goofy.tunabank.v1.domain.Enum.AccountType;
import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import lombok.Data;

@Data
public class CreateAccountRequestDto {

    private AccountType accountType;

    private String accountPassword;

    private CurrencyType currencyType;

}
