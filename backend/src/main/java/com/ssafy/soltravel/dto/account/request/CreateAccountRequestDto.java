package com.ssafy.soltravel.dto.account.request;

import lombok.Data;

@Data
public class CreateAccountRequestDto {

    private String currencyCode;

    private String currencyName;

}
