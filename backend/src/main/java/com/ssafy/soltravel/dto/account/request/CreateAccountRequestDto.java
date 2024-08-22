package com.ssafy.soltravel.dto.account.request;

import com.ssafy.soltravel.domain.Enum.AccountType;
import lombok.Data;

@Data
public class CreateAccountRequestDto {

    private AccountType accountType;

    private String currencyCode;
}
