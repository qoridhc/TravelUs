package com.ssafy.soltravel.dto.account.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteAccountResponseDto {

    private String status;

    private String accountNo;

    private String refundAccountNo;

    private String accountBalance;

}
