package com.goofy.tunabank.v1.exception;

import com.goofy.tunabank.v1.dto.ErrorResponse;
import com.goofy.tunabank.v1.exception.Transaction.InsufficientBalanceException;
import com.goofy.tunabank.v1.exception.Transaction.InvalidWithdrawalAmount;
import com.goofy.tunabank.v1.util.LogUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler {

  @ExceptionHandler(InvalidWithdrawalAmount.class)
  public ResponseEntity<ErrorResponse> handleInvalidWithdrawalAmount(InvalidWithdrawalAmount ex) {
    LogUtil.error("Invalid withdrawal amount: {}", ex.getMessage());
    ErrorResponse errorResponse = new ErrorResponse("INVALID_WITHDRAWAL_AMOUNT", ex.getMessage());
    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(InsufficientBalanceException.class)
  public ResponseEntity<ErrorResponse> handleInsufficientBalanceException(
      InsufficientBalanceException ex) {
    LogUtil.error("Insufficient balance: {}", ex.getMessage());
    ErrorResponse errorResponse = new ErrorResponse("INSUFFICIENT_BALANCE", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }
}
