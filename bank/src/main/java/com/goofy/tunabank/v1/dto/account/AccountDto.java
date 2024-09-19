package com.goofy.tunabank.v1.dto.account;

import com.goofy.tunabank.v1.domain.Enum.AccountType;
import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto{

	private Long accountId;

	private String accountNo;

	private String accountPassword;

	private double balance;

	private AccountType accountType;

	private int bankCode;

	private CurrencyType currencyCode;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

}