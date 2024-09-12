package com.goofy.tunabank.controller;

import com.goofy.tunabank.dto.ResponseDto;
import com.goofy.tunabank.dto.account.request.CreateAccountRequestDto;
import com.goofy.tunabank.dto.account.response.CreateAccountResponseDto;
import com.goofy.tunabank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/{userId}")
    public ResponseEntity<CreateAccountResponseDto> crateNewAccount(
        @RequestBody CreateAccountRequestDto requestDto
    ) {

        CreateAccountResponseDto responseDto = accountService.crateNewAccount(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }



}
