package com.goofy.tunabank.v1.mapper;

import com.goofy.tunabank.v1.domain.Account;
import com.goofy.tunabank.v1.dto.account.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "id", target = "accountId")
    AccountDto toDto(Account account);

}
