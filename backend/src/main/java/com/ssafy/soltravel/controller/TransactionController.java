package com.ssafy.soltravel.controller;

import com.ssafy.soltravel.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.dto.transaction.response.DepositResponseDto;
import com.ssafy.soltravel.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    // 계좌 입금
    @PostMapping("/{accountNo}/deposit")
    public ResponseEntity<DepositResponseDto> postAccountDeposit(
        @PathVariable String accountNo,
        @RequestBody TransactionRequestDto requestDto
    ) {
        ResponseEntity<DepositResponseDto> response = transactionService.postAccountDeposit(accountNo, requestDto);

        return response;
    }

    // 계좌 출금
    @PostMapping("/{accountNo}/withdraw")
    public ResponseEntity<DepositResponseDto> postAccountWithdrawal(
        @PathVariable String accountNo,
        @RequestBody TransactionRequestDto requestDto
    ) {
        ResponseEntity<DepositResponseDto> response = transactionService.postAccountWithdrawal(accountNo, requestDto);

        return response;
    }

}
