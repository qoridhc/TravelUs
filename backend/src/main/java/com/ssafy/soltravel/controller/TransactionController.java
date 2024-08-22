package com.ssafy.soltravel.controller;

import com.ssafy.soltravel.dto.transaction.TransactionHistoryDto;
import com.ssafy.soltravel.dto.transaction.request.TransactionHistoryRequestDto;
import com.ssafy.soltravel.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.dto.transaction.request.TransferRequestDto;
import com.ssafy.soltravel.dto.transaction.response.DepositResponseDto;
import com.ssafy.soltravel.dto.transaction.response.TransferHistoryResponseDto;
import com.ssafy.soltravel.service.transaction.TransactionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    // 계좌 이체
    @PostMapping("/{accountNo}/transfer")
    public ResponseEntity<List<TransferHistoryResponseDto>> postAccountTransfer(
        @PathVariable String accountNo,
        @RequestBody TransferRequestDto requestDto
    ) {
        ResponseEntity<List<TransferHistoryResponseDto>> response = transactionService.postAccountTransfer(accountNo, requestDto);

        return response;
    }


    // 거래 내역 조회
    @GetMapping("/{accountNo}/history")
    public ResponseEntity<List<TransactionHistoryDto>> getHistoryByAccountNo(
        @PathVariable String accountNo,
        @RequestBody TransactionHistoryRequestDto requestDto
    ) {

        ResponseEntity<List<TransactionHistoryDto>> response = transactionService.getHistoryByAccountNo(accountNo, requestDto);

        return response;
    }


}
