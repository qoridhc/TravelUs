package com.ssafy.soltravel.dto.account;

import lombok.Data;

@Data
public class AccountDto {

    private String bankCode;
    private String dailyTransferLimit;
    private String accountName;
    private String accountTypeCode;
    private String bankName;
    private String accountTypeName;
    private String accountExpiryDate;
    private String userName;
    private String oneTimeTransferLimit;
    private String accountCreatedDate;
    private String lastTransactionDate;
    private String accountNo;
    private String currency;
    private String accountBalance;
}