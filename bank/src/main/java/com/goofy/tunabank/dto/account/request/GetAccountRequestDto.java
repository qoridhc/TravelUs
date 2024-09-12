package com.goofy.tunabank.dto.account.request;

import com.goofy.tunabank.domain.Enum.AccountType;
import lombok.Getter;

@Getter
public class GetAccountRequestDto {
    AccountType accountType;
}
