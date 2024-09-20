package com.goofy.tunabank.v1.exception.account;

import com.goofy.tunabank.v1.exception.CustomException;

public class RefundAccountRequiredException extends CustomException {

    private static final String DEFAULT_CODE = "REFUND_ACCOUNT_REQUIRED";
    private static final int DEFAULT_STATUS = 400;

    public RefundAccountRequiredException() {
        super(
            String.format("머니 박스에 금액이 있을 시 환불 계좌는 필수입니다"),
            DEFAULT_CODE,
            DEFAULT_STATUS
        );
    }
}