package com.goofy.tunabank.v1.controller;

import com.goofy.tunabank.v1.dto.account.AccountDto;
import com.goofy.tunabank.v1.dto.account.request.AddMoneyBoxRequestDto;
import com.goofy.tunabank.v1.dto.account.request.CreateGeneralAccountRequestDto;
import com.goofy.tunabank.v1.dto.account.request.InquireAccountRequestDto;
import com.goofy.tunabank.v1.dto.moneyBox.MoneyBoxDto;
import com.goofy.tunabank.v1.service.AccountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Controller
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // 일반 계좌 생성 ( 개인 or 모임 )
    @PostMapping("/{userId}")
    public ResponseEntity<AccountDto> createNewAccount(
        @PathVariable Long userId,
        @RequestBody CreateGeneralAccountRequestDto requestDto
    ) {

        AccountDto responseDto = accountService.createGeneralAccount(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 모임 통장 머니박스 추가
    @PostMapping("/addMoneyBox")
    public ResponseEntity<List<MoneyBoxDto>> addAccountMoneyBox(
        @RequestBody AddMoneyBoxRequestDto requestDto
    ) {

        List<MoneyBoxDto> responseDto = accountService.addAccountMoneyBox(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 계좌 조회
    @PostMapping("/inquireAccount")
    public ResponseEntity<AccountDto> inquireAccount(
        @RequestBody InquireAccountRequestDto requestDto
    ) {
        AccountDto responseDto = accountService.inquireAccount(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


}
