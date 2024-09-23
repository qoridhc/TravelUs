package com.ssafy.soltravel.v2.dto.account.request;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import lombok.Data;

@Data
public class AddMoneyBoxRequestDto {

    private Long userId;

    private String accountNo;

    private String accountPassword;

    private CurrencyType currencyCode;

}
