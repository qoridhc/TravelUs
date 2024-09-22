package com.ssafy.soltravel.v2.dto.account;

import com.ssafy.soltravel.v1.domain.Enum.AccountType;
import com.ssafy.soltravel.v2.dto.moneyBox.MoneyBoxDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

	private Long accountId;

	private String accountNo;

	private String accountPassword;

	private String accountType;

	private int bankCode;

	private List<MoneyBoxDto> moneyBoxDtos;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

}