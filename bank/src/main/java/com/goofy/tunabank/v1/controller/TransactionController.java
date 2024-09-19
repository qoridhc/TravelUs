package com.goofy.tunabank.v1.controller;

import com.goofy.tunabank.v1.dto.transaction.request.TransactionHistoryRequestDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionRequestDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransferRequestDto;
import com.goofy.tunabank.v1.dto.transaction.response.TransactionResponseDto;
import com.goofy.tunabank.v1.dto.transaction.response.TransferResponseDto;
import com.goofy.tunabank.v1.service.TransactionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

  private final TransactionService transactionService;

  /**
   * 입금 및 출금
   */
  @PostMapping
  public ResponseEntity<TransactionResponseDto> processTransaction(
      @RequestBody TransactionRequestDto requestDto) {

    return ResponseEntity.ok().body(transactionService.processTransaction(requestDto));
  }

  /**
   * 이체
   */
  @PostMapping("/transfer")
  public ResponseEntity<TransferResponseDto> processTransfer(
      @RequestBody TransferRequestDto requestDto) {

    return ResponseEntity.ok().body(transactionService.processTransfer(requestDto));
  }

  /**
   * 거래 내역 조회
   */
  @PostMapping("/history")
  public ResponseEntity<List<TransactionResponseDto>> getTransactionHistory(
      @RequestBody TransactionHistoryRequestDto requestDto) {

    return ResponseEntity.ok().body(transactionService.getTransactionHistory(requestDto));
  }
}