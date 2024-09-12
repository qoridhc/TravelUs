package com.goofy.tunabank.exception.account;

import com.goofy.tunabank.domain.Enum.AccountType;

public class InvalidAccountIdOrTypeException extends RuntimeException {

    public InvalidAccountIdOrTypeException(Long accountId, AccountType accountType) {
        super(String.format("유효하지 않은 계좌 정보 id : %d , type : %s", accountId, accountType));
    }
}
