package com.ssafy.soltravel.mapper;

import com.ssafy.soltravel.domain.Currency;
import com.ssafy.soltravel.domain.Enum.AccountType;
import com.ssafy.soltravel.domain.Enum.CurrencyType;
import com.ssafy.soltravel.domain.ForeignAccount;
import com.ssafy.soltravel.domain.GeneralAccount;
import com.ssafy.soltravel.domain.User;
import com.ssafy.soltravel.dto.account.AccountDto;
import com.ssafy.soltravel.dto.account.request.CreateAccountRequestDto;
import com.ssafy.soltravel.dto.currency.CurrencyDto;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@RequiredArgsConstructor
public class AccountMapper {

    public static AccountDto toCreateAccountDto(GeneralAccount generalAccount) {

        CurrencyDto currencyDto = new CurrencyDto("KRW", "원화");

        AccountDto accountDto = AccountDto.builder()
            .id(generalAccount.getId())
            .bankCode(generalAccount.getBankCode())
            .accountPassword(generalAccount.getAccountPassword())
            .accountName(generalAccount.getAccountName())
            .accountType(generalAccount.getAccountType())
            .accountNo(generalAccount.getAccountNo())
            .groupName(generalAccount.getGroupName())
            .iconName(generalAccount.getIconName())
            .travelStartDate(generalAccount.getTravelStartDate())
            .travelEndDate(generalAccount.getTravelEndDate())
            .currency(currencyDto)
            .createdAt(String.valueOf(generalAccount.getCreatedAt()))
            .updatedAt(String.valueOf(generalAccount.getUpdatedAt()))
            .build();

        return accountDto;
    }

    public static AccountDto toCreateAccountDto(ForeignAccount foreignAccount) {

        CurrencyDto currencyDto = new CurrencyDto(
            foreignAccount.getCurrency().getCurrencyCode(),
            foreignAccount.getCurrency().getCurrencyName()
        );

        AccountDto accountDto = AccountDto.builder()
            .id(foreignAccount.getId())
            .bankCode(foreignAccount.getBankCode())
            .accountPassword(foreignAccount.getAccountPassword())
            .accountName("신한은행 외화 모임통장")
            .accountType(AccountType.GROUP)
            .accountNo(foreignAccount.getAccountNo())
            .groupName(foreignAccount.getGroupName())
            .travelStartDate(foreignAccount.getTravelStartDate())
            .travelEndDate(foreignAccount.getTravelEndDate())
            .iconName(foreignAccount.getIconName())
            .currency(currencyDto)
            .createdAt(String.valueOf(foreignAccount.getCreatedAt()))
            .updatedAt(String.valueOf(foreignAccount.getUpdatedAt()))
            .build();

        return accountDto;
    }

    public static ForeignAccount toForeignAccountEntitiy(
        Map<String, String> recObject,
        GeneralAccount generalAccount,
        CreateAccountRequestDto requestDto
    ) {

        CurrencyType currencyType = CurrencyType.fromCode(requestDto.getCurrencyCode());

        Currency currency = new Currency();
        currency.setCurrencyCode(currencyType.getCurrencyCode());
        currency.setCurrencyName(currencyType.getCurrencyName());

        ForeignAccount foreignAccount = ForeignAccount.builder()
            .bankCode(Integer.parseInt(recObject.get("bankCode")))
            .accountNo(recObject.get("accountNo"))
            .accountPassword(requestDto.getAccountPassword())
            .balance(0.0)
            .generalAccount(generalAccount)
            .travelStartDate(requestDto.getTravelStartDate())
            .travelEndDate(requestDto.getTravelEndDate())
            .groupName(requestDto.getGroupName())
            .iconName(requestDto.getIconName())
            .currency(currency)
            .build();

        return foreignAccount;
    }


    public static GeneralAccount toGeneralAccountEntitiy(
        Map<String, String> recObject,
        User user,
        CreateAccountRequestDto requestDto
    ) {

        ModelMapper modelMapper = new ModelMapper();

        GeneralAccount generalAccount = modelMapper.map(recObject, GeneralAccount.class);

        generalAccount.setBalance(0.0);
        generalAccount.setUser(user);
        generalAccount.setAccountType(requestDto.getAccountType());
        generalAccount.setAccountPassword(requestDto.getAccountPassword());

        if(generalAccount.getAccountType().equals(AccountType.INDIVIDUAL)) {
            generalAccount.setAccountName("신한은행 일반 개인통장");
        }else{
            generalAccount.setTravelStartDate(requestDto.getTravelStartDate());
            generalAccount.setTravelEndDate(requestDto.getTravelEndDate());
            generalAccount.setGroupName(requestDto.getGroupName());
            generalAccount.setAccountName("신한은행 일반 모임통장");
            generalAccount.setIconName(requestDto.getIconName());
        }

        return generalAccount;
    }



}
