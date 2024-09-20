package com.goofy.tunabank.v1.dto.account.request;

import com.goofy.tunabank.v1.domain.Enum.AccountType;
import lombok.Data;

@Data
public class CreateGeneralAccountRequestDto {

    private AccountType accountType;

    private String accountPassword;

    private Long bankId;

}
