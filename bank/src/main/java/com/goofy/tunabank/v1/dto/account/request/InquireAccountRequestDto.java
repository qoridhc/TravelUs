package com.goofy.tunabank.v1.dto.account.request;

import lombok.Data;

@Data
public class InquireAccountRequestDto {

    private Long accountId;

    private String accountPassword;

}
