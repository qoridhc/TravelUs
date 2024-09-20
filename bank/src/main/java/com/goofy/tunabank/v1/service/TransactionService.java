package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import com.goofy.tunabank.v1.domain.MoneyBox;
import com.goofy.tunabank.v1.domain.TransactionHistory;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionHistoryRequestDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionRequestDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransferRequestDto;
import com.goofy.tunabank.v1.dto.transaction.response.TransactionResponseDto;
import com.goofy.tunabank.v1.exception.transaction.InsufficientBalanceException;
import com.goofy.tunabank.v1.exception.transaction.InvalidTransactionTypeException;
import com.goofy.tunabank.v1.exception.transaction.InvalidWithdrawalAmountException;
import com.goofy.tunabank.v1.exception.transaction.MoneyBoxNotFoundException;
import com.goofy.tunabank.v1.exception.transaction.TransactionHistoryNotFoundException;
import com.goofy.tunabank.v1.mapper.TransactionMapper;
import com.goofy.tunabank.v1.repository.MoneyBoxRepository;
import com.goofy.tunabank.v1.repository.transaction.TransactionHistoryRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

  private final TransactionHistoryRepository transactionHistoryRepository;
  private final MoneyBoxRepository moneyBoxRepository;
  private final TransactionMapper transactionMapper;
  private static final int KRW_CURRENCY_ID = 1;

  //TODO: userKey확인 및 비밀번호 체크

  /**
   * 입금 및 출금(원화, 외화)
   */
  @Transactional
  public TransactionResponseDto processTransaction(TransactionRequestDto requestDto) {

    Long accountId = requestDto.getAccountId();
    int currencyId = requestDto.getCurrencyId();
    MoneyBox moneyBox = moneyBoxRepository.findMoneyBoxByAccountAndCurrency(accountId, currencyId)
        .orElseThrow(() -> new MoneyBoxNotFoundException(accountId, currencyId));

    TransactionType transactionType = requestDto.getTransactionType();
    double amount = requestDto.getTransactionBalance();
    double afterBalance = 0L;

    // 입금 또는 출금 처리
    afterBalance = switch (transactionType) {
      case D -> processDeposit(moneyBox, amount);
      case W -> processWithdrawal(moneyBox, amount);
      default -> throw new InvalidTransactionTypeException(transactionType);
    };

    // 거래 기록을 위한 다음 id값 추출
    Long nextId = transactionHistoryRepository.findMaxAccountId();
    if (nextId == null) {
      nextId = 0L;
    }
    nextId++;

    TransactionHistory transactionHistory = TransactionHistory.createTransactionHistory(nextId,
        transactionType, moneyBox, null, requestDto.getHeader().getTransmissionDateTime(), amount,
        afterBalance, requestDto.getTransactionSummary());

    TransactionHistory th = transactionHistoryRepository.save(transactionHistory);
    return transactionMapper.convertTransactionHistoryToTransactionResponseDto(th);
  }

  /**
   * 이체 처리 :: 원화만 가능
   */
  @Transactional
  public List<TransactionResponseDto> processTransfer(TransferRequestDto requestDto) {

    long withdrawalAccountId = requestDto.getWithdrawalAccountId();
    long depositAccountId = requestDto.getDepositAccountId();
    double amount = requestDto.getTransactionBalance();

    String withdrawalSummary = requestDto.getWithdrawalTransactionSummary();
    String depositSummary = requestDto.getDepositTransactionSummary();

    // 출금 머니박스
    MoneyBox withdrawalBox = moneyBoxRepository.findMoneyBoxByAccountAndCurrency(
            withdrawalAccountId, KRW_CURRENCY_ID)
        .orElseThrow(() -> new MoneyBoxNotFoundException(withdrawalAccountId, KRW_CURRENCY_ID));

    // 입금 머니박스
    MoneyBox depositBox = moneyBoxRepository.findMoneyBoxByAccountAndCurrency(depositAccountId,
            KRW_CURRENCY_ID)
        .orElseThrow(() -> new MoneyBoxNotFoundException(withdrawalAccountId, KRW_CURRENCY_ID));

    // 출금 처리
    double withdrawalAfterBalance = processWithdrawal(withdrawalBox, amount);

    // 입금 처리
    double depositAfterBalance = processDeposit(depositBox, amount);

    // 다음 id값 추출
    Long nextId = transactionHistoryRepository.findMaxAccountId();
    if (nextId == null) {
      nextId = 0L;
    }
    nextId++;

    LocalDateTime transmissionDateTime = requestDto.getHeader().getTransmissionDateTime();

    // 출금 기록 저장
    TransactionHistory withdrawalTransactionHistory = TransactionHistory.createTransactionHistory(
        nextId, TransactionType.TW, withdrawalBox, depositBox.getAccount().getAccountNo(),
        transmissionDateTime, amount, withdrawalAfterBalance,withdrawalSummary);

    TransactionHistory withdrawalTh = transactionHistoryRepository.save(
        withdrawalTransactionHistory);

    // 입금 기록 저장
    TransactionHistory depositTransactionHistory = TransactionHistory.createTransactionHistory(
        nextId, TransactionType.TD, depositBox, withdrawalBox.getAccount().getAccountNo(),
        transmissionDateTime, amount, depositAfterBalance,depositSummary);

    TransactionHistory depositTh = transactionHistoryRepository.save(depositTransactionHistory);

    // response 변환
    return transactionMapper.convertTransactionHistoriesToResponseDtos(List.of(withdrawalTh, depositTh));
  }

  /**
   * 복합키와 날짜 범위를 고려한 동적 쿼리
   */
  @Transactional(readOnly = true)
  public List<TransactionResponseDto> getTransactionHistory(
      TransactionHistoryRequestDto requestDto) {

    List<TransactionHistory> transactionHistories = transactionHistoryRepository.findByCustomOrder(
        requestDto).orElseThrow(TransactionHistoryNotFoundException::new);

    return transactionMapper.convertTransactionHistoriesToResponseDtos(transactionHistories);
  }

  /**
   * ------------------------------------------------------------------------------------------
   * 아래 부터는 업데이트 관련 로직
   */

  /**
   * 입금 처리
   */
  @Transactional
  public double processDeposit(MoneyBox moneyBox, double amount) {
    return updateAccountBalance(moneyBox, amount, true);
  }

  /**
   * 출금 처리
   */
  @Transactional
  public double processWithdrawal(MoneyBox moneyBox, double amount)
      throws InvalidWithdrawalAmountException {

    // 출금 금액 검증
    validateWithdrawal(moneyBox.getBalance(), amount);
    return updateAccountBalance(moneyBox, amount, false);
  }


  @Transactional
  public double updateAccountBalance(MoneyBox moneyBox, double amount, boolean isDeposit) {
    double currentBalance = moneyBox.getBalance();
    double afterBalance = isDeposit ? currentBalance + amount : currentBalance - amount;
    moneyBox.setBalance(afterBalance);
    moneyBox.setUpdatedAt(LocalDateTime.now());
    moneyBoxRepository.save(moneyBox);
    return afterBalance;
  }


  /**
   * 출금시 유효성 검증
   */
  private void validateWithdrawal(double currentBalance, double amount) {
    if (amount <= 0) {
      throw new InvalidWithdrawalAmountException(amount);
    }

    if (amount > currentBalance) {
      throw new InsufficientBalanceException(currentBalance, amount);
    }
  }
}
