package com.goofy.tunabank.mapper;

import com.goofy.tunabank.domain.Account;
import com.goofy.tunabank.dto.account.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "id", target = "accountId")  // 복합키의 accountId 필드 매핑
    @Mapping(source = "currency.currencyCode", target = "currencyCode")
    AccountDto toDto(Account account);

}
