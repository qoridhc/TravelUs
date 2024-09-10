package com.goofy.tunabank.dto.account.request;

import com.goofy.tunabank.domain.Enum.AccountType;
import com.goofy.tunabank.domain.Enum.CurrencyType;
import lombok.Data;

@Data
public class CreateAccountRequestDto {

    private AccountType accountType;

    private CurrencyType currencyType;

}
