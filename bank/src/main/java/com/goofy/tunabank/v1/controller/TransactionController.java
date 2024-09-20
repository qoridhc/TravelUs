package com.goofy.tunabank.v1.controller;

import com.goofy.tunabank.v1.common.RecWrapper;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionHistoryRequestDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionRequestDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransferRequestDto;
import com.goofy.tunabank.v1.dto.transaction.response.TransactionResponseDto;
import com.goofy.tunabank.v1.service.TransactionService;
import com.goofy.tunabank.v1.util.LogUtil;
import java.util.List;
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
   * 입금
   */
  @PostMapping("/deposit")
  public ResponseEntity<RecWrapper<TransactionResponseDto>> deposit(
      @RequestBody TransactionRequestDto requestDto) {

    TransactionResponseDto response = transactionService.processTransaction(requestDto);
    return ResponseEntity.ok(new RecWrapper<>(response));
  }

  /**
   * 출금
   */
  @PostMapping("/withdrawal")
  public ResponseEntity<RecWrapper<TransactionResponseDto>> withdrawal(
      @RequestBody TransactionRequestDto requestDto) {

    TransactionResponseDto response = transactionService.processTransaction(requestDto);
    return ResponseEntity.ok(new RecWrapper<>(response));
  }

  /**
   * 이체
   */
  @PostMapping("/transfer")
  public ResponseEntity<RecWrapper<List<TransactionResponseDto>>> transfer(
      @RequestBody TransferRequestDto requestDto) {

    List<TransactionResponseDto> response = transactionService.processTransfer(requestDto);
    return ResponseEntity.ok(new RecWrapper<>(response));
  }

  /**
   * 거래 내역 조회
   */
  @PostMapping("/history")
  public ResponseEntity<RecWrapper<List<TransactionResponseDto>>> getTransactionHistory(
      @RequestBody TransactionHistoryRequestDto requestDto) {

    List<TransactionResponseDto> response = transactionService.getTransactionHistory(requestDto);
    return ResponseEntity.ok(new RecWrapper<>(response));
  }

  /**
   * 원화 <-> 외화 이체
   */
  @PostMapping("/moneyboxes/transfer")
  public ResponseEntity<RecWrapper<?>> transferBetweenMoneyBoxes(@RequestBody TransferRequestDto requestDto) {


    return null;
  }

  /**
   * 환전 예상 금액 조회
   */
}
