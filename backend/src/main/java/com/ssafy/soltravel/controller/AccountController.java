package com.ssafy.soltravel.controller;

import com.ssafy.soltravel.dto.account.AccountDto;
import com.ssafy.soltravel.dto.account.response.CreateAccountResponseDto;
import com.ssafy.soltravel.dto.account.response.DeleteAccountResponseDto;
import com.ssafy.soltravel.service.account.AccountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/{userId}")
    public ResponseEntity<CreateAccountResponseDto> createAccount(@PathVariable Long userId) {

        ResponseEntity<CreateAccountResponseDto> responseEntity = accountService.createAccount(userId);

        return responseEntity;
    }

    @GetMapping("/{userId}/all")
    public ResponseEntity<List<AccountDto>> getAllByUserId(@PathVariable Long userId) {

        ResponseEntity<List<AccountDto>> responseEntity = accountService.getAllByUserId(userId);

        return responseEntity;
    }

    @DeleteMapping("/{accountNo}")
    public ResponseEntity<DeleteAccountResponseDto> deleteAccount(@PathVariable String accountNo) {

        ResponseEntity<DeleteAccountResponseDto> responseEntity = accountService.deleteAccount(accountNo);

        return responseEntity;
    }

}
