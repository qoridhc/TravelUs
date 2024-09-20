package com.goofy.tunabank.v1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
   * 입금 및 출금
   */
  @PostMapping
  public ResponseEntity<RecWrapper<TransactionResponseDto>> processTransaction(
      @RequestBody TransactionRequestDto requestDto) {

    TransactionResponseDto response = transactionService.processTransaction(requestDto);
    return ResponseEntity.ok(new RecWrapper<>(response));
  }

  /**
   * 이체
   */
  @PostMapping("/transfer")
  public ResponseEntity<RecWrapper<List<TransactionResponseDto>>> processTransfer(
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
    LogUtil.info("response: {}", response);
    return ResponseEntity.ok(new RecWrapper<>(response));
  }
}
