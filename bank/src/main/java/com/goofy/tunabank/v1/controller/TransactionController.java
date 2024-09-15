package com.goofy.tunabank.v1.controller;

import com.goofy.tunabank.v1.dto.transaction.request.TransactionRequestDto;
import com.goofy.tunabank.v1.dto.transaction.response.TransactionResponseDto;
import com.goofy.tunabank.v1.exception.Transaction.InvalidWithdrawalAmount;
import com.goofy.tunabank.v1.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
  @PostMapping("/deposit")
  public ResponseEntity<TransactionResponseDto> processTransaction(@RequestBody TransactionRequestDto requestDto)
      throws InvalidWithdrawalAmount {

    return ResponseEntity.ok().body(transactionService.processTransaction(requestDto));
  }
}