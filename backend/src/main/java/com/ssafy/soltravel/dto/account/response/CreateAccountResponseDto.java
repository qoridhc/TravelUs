package com.ssafy.soltravel.dto.account.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.soltravel.dto.account.CreateAccountDto;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // null인거 제외하고 직렬화
public class CreateAccountResponseDto {

    private CreateAccountDto generalAccount;

    private CreateAccountDto foreignAccount;

}