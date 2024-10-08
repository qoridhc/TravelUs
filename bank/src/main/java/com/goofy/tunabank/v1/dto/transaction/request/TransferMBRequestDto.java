package com.goofy.tunabank.v1.dto.transaction.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goofy.tunabank.v1.common.Header;
import com.goofy.tunabank.v1.domain.Account;
import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.domain.Enum.TransferType;
import com.goofy.tunabank.v1.domain.MoneyBox;
import com.goofy.tunabank.v1.dto.moneyBox.request.DeleteMoneyBoxRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferMBRequestDto {

    //헤더
    @JsonProperty("Header")
    private Header header;

    //이체 종류
    private TransferType transferType;

    //계좌번호
    private String accountNo;

    //계좌 비밀번호
    private String accountPassword;

    //변경 전 통화
    private CurrencyType sourceCurrencyCode;

    //변경 후 통화
    private CurrencyType targetCurrencyCode;

    //신청 금액
    private double transactionBalance;

    // 팩토리 메서드 정의
    public static TransferMBRequestDto from(Account account, MoneyBox moneyBox, DeleteMoneyBoxRequestDto requestDto) {

        return new TransferMBRequestDto(
            requestDto.getHeader(),
            TransferType.M,
            account.getAccountNo(),
            account.getAccountPassword(),
            requestDto.getCurrencyCode(),
            CurrencyType.KRW,
            moneyBox.getBalance()
        );
    }
}
