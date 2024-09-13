package com.goofy.tunabank.v1.controller;

import com.goofy.tunabank.v1.dto.account.AccountDto;
import com.goofy.tunabank.v1.dto.account.request.CreateAccountRequestDto;
import com.goofy.tunabank.v1.dto.account.request.GetAccountRequestDto;
import com.goofy.tunabank.v1.dto.account.response.CreateAccountResponseDto;
import com.goofy.tunabank.v1.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

    // 계좌 생성
    @PostMapping("/{userId}")
    public ResponseEntity<CreateAccountResponseDto> crateNewAccount(
        @PathVariable Long userId,
        @RequestBody CreateAccountRequestDto requestDto
    ) {

        CreateAccountResponseDto responseDto = accountService.crateNewAccount(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccountByIdAndType(
        @PathVariable Long accountId,
        @RequestBody GetAccountRequestDto requestDto
    ) {

        AccountDto responseDto = accountService.getAccountByIdAndType(accountId, requestDto.getAccountType());

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

}
