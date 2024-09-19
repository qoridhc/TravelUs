package com.goofy.tunabank.v1.exception.account;

import com.goofy.tunabank.v1.exception.CustomException;

public class InvalidGroupAccountIdException extends CustomException {
    private static final String DEFAULT_CODE = "GRUOP_ACCOUNT_ID_INVALID";
    private static final int DEFAULT_STATUS = 404;

    public InvalidGroupAccountIdException(Long accountId) {
        super(
            String.format("Group AccountId Does Not Exist: %s", accountId),
            DEFAULT_CODE,
            DEFAULT_STATUS
        );
    }
}