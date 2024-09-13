package com.goofy.tunabank.dto.account;

import com.goofy.tunabank.domain.Currency;
import com.goofy.tunabank.domain.Enum.AccountType;
import com.goofy.tunabank.domain.Enum.CurrencyType;
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