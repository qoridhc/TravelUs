package com.ssafy.soltravel.mapper;

import com.ssafy.soltravel.domain.Currency;
import com.ssafy.soltravel.domain.CurrencyType;
import com.ssafy.soltravel.domain.ForeignAccount;
import com.ssafy.soltravel.domain.GeneralAccount;
import com.ssafy.soltravel.dto.account.CreateAccountDto;
import com.ssafy.soltravel.dto.account.request.CreateAccountRequestDto;
import com.ssafy.soltravel.dto.currency.CurrencyDto;
import java.util.Map;
import org.modelmapper.ModelMapper;

public class AccountMapper {

    public static CreateAccountDto toCreateAccountDto(GeneralAccount generalAccount){

        CurrencyDto currencyDto = new CurrencyDto("KRW", "원화");

        CreateAccountDto accountDto = CreateAccountDto.builder()
            .bankCode(generalAccount.getBankCode())
            .accountNo(generalAccount.getAccountNo())
            .currency(currencyDto)
            .build();

        return accountDto;
    }

    public static CreateAccountDto toCreateAccountDto(ForeignAccount foreignAccount){

        CurrencyDto currencyDto = new CurrencyDto(
            foreignAccount.getCurrency().getCurrencyCode(),
            foreignAccount.getCurrency().getCurrencyName()
        );

        CreateAccountDto accountDto = CreateAccountDto.builder()
            .bankCode(foreignAccount.getBankCode())
            .accountNo(foreignAccount.getAccountNo())
            .currency(currencyDto)
            .build();

        return accountDto;
    }

    public static ForeignAccount toForeignAccountEntitiy(
        Map<String,String> recObject,
        GeneralAccount generalAccount,
        CreateAccountRequestDto requestDto
    ){
        ModelMapper modelMapper = new ModelMapper();

        CurrencyType currencyType = CurrencyType.fromCode(requestDto.getCurrencyCode());

        Currency currency = new Currency();
        currency.setCurrencyCode(currencyType.getCurrencyCode());
        currency.setCurrencyName(currencyType.getCurrencyName());

        // REC 데이터를 GeneralAccount 엔티티로 변환
        ForeignAccount foreignAccount = ForeignAccount.builder()
            .bankCode(Integer.parseInt(recObject.get("bankCode")))
            .accountNo(recObject.get("accountNo"))
            .balance(0L)
            .generalAccount(generalAccount)
            .currency(currency)
            .build();

        return foreignAccount;
    }

}
