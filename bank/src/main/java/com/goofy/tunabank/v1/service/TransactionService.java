package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.Account;
import com.goofy.tunabank.v1.domain.Enum.AccountType;
import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import com.goofy.tunabank.v1.domain.Merchant;
import com.goofy.tunabank.v1.domain.TransactionHistory;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionRequestDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransferRequestDto;
import com.goofy.tunabank.v1.dto.transaction.response.TransactionResponseDto;
import com.goofy.tunabank.v1.dto.transaction.response.TransferResponseDto;
import com.goofy.tunabank.v1.exception.Transaction.InsufficientBalanceException;
import com.goofy.tunabank.v1.exception.Transaction.InvalidMerchantIdException;
import com.goofy.tunabank.v1.exception.Transaction.InvalidWithdrawalAmount;
import com.goofy.tunabank.v1.exception.account.InvalidAccountIdOrTypeException;
import com.goofy.tunabank.v1.mapper.TransactionMapper;
import com.goofy.tunabank.v1.repository.AccountRepository;
import com.goofy.tunabank.v1.repository.MerchantRepository;
import com.goofy.tunabank.v1.repository.TransactionHistoryRepository;
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
  private final MerchantRepository merchantRepository;
  private final AccountRepository accountRepository;
  private final TransactionMapper transactionMapper;

  /**
   * 입금 및 출금
   */
  @Transactional
  public TransactionResponseDto processTransaction(TransactionRequestDto requestDto) {

    Long accountId = requestDto.getAccountId();
    AccountType accountType = requestDto.getAccountType();

    Account account = accountRepository.findByIdAndAccountType(accountId, accountType)
        .orElseThrow(() -> new InvalidAccountIdOrTypeException(accountId, accountType));

    TransactionType transactionType = requestDto.getTransactionType();
    double amount = requestDto.getTransactionBalance();
    double afterBalance = 0L;

    // 입금 또는 출금 처리
    if (transactionType == TransactionType.D) {
      afterBalance = processDeposit(account, amount);
    } else if (transactionType == TransactionType.W) {
      afterBalance = processWithdrawal(account, amount);
    } else {
      throw new IllegalArgumentException("Invalid transaction type");
    }

    // 거래 기록을 위한 다음 id값 추출
    Long nextId = transactionHistoryRepository.findMaxAccountId();
    if (nextId == null) {
      nextId = 0L;
    }
    nextId++;

    long merchantId = transactionType == TransactionType.D ? 1L : 2L;
    Merchant merchant = merchantRepository.findById(merchantId)
        .orElseThrow(() -> new InvalidMerchantIdException(merchantId));

    TransactionHistory transactionHistory = TransactionHistory.builder()
        .id(nextId)
        .transactionType(transactionType)
        .account(account)
        .merchant(merchant)
        .transactionAt(LocalDateTime.now())
        .amount(amount)
        .balance(afterBalance)
        .summary(requestDto.getTransactionSummary())
        .build();

    TransactionHistory th = transactionHistoryRepository.save(transactionHistory);
    return transactionMapper.convertTransactionHistoryToTransactionResponseDto(th);
  }

  /**
   * 이체 처리
   */
  @Transactional
  public TransferResponseDto processTransfer(TransferRequestDto requestDto) {

    Long withdrawalAccountId = requestDto.getWithdrawalAccountId();
    Long depositAccountId = requestDto.getDepositAccountId();
    AccountType withdrawalAccountType = requestDto.getWithdrawalAccountType();
    AccountType depositAccountType = requestDto.getDepositAccountType();
    double amount = requestDto.getTransactionBalance();
    String summary = requestDto.getTransactionSummary();

    // 출금 계좌
    Account withdrawalAccount = accountRepository.findByIdAndAccountType(withdrawalAccountId,
            withdrawalAccountType)
        .orElseThrow(
            () -> new InvalidAccountIdOrTypeException(withdrawalAccountId, withdrawalAccountType));

    // 입금 계좌
    Account depositAccount = accountRepository.findByIdAndAccountType(depositAccountId,
            depositAccountType)
        .orElseThrow(
            () -> new InvalidAccountIdOrTypeException(depositAccountId, depositAccountType));

    // 출금 처리
    double withdrawalAfterBalance = processWithdrawal(withdrawalAccount, amount);

    // 입금 처리
    double depositAfterBalance = processDeposit(depositAccount, amount);

    // 다음 id값 추출
    Long nextId = transactionHistoryRepository.findMaxAccountId();
    if (nextId == null) {
      nextId = 0L;
    }
    nextId++;

    long merchantId = 3L;
    Merchant merchant = merchantRepository.findById(merchantId)
        .orElseThrow(() -> new InvalidMerchantIdException(merchantId));

    // 출금 기록 저장
    TransactionHistory withdrawalTransactionHistory = TransactionHistory.builder()
        .id(nextId)
        .transactionType(TransactionType.TW)
        .account(withdrawalAccount)
        .merchant(merchant)
        .transactionAt(LocalDateTime.now())//TODO:추후변경예정
        .amount(amount)
        .balance(withdrawalAfterBalance)
        .summary(summary)
        .build();
    TransactionHistory withdrawalTh=transactionHistoryRepository.save(withdrawalTransactionHistory);

    // 입금 기록 저장
    TransactionHistory depositTransactionHistory = TransactionHistory.builder()
        .id(nextId)
        .transactionType(TransactionType.TD)
        .account(depositAccount)
        .merchant(merchant)
        .transactionAt(LocalDateTime.now())
        .amount(amount)
        .balance(depositAfterBalance)
        .build();
    TransactionHistory depositTh=transactionHistoryRepository.save(depositTransactionHistory);

    // response 변환
   return transactionMapper.convertToTransferResponseDto(withdrawalTh, depositTh);
  }

  /**
   * 아래 부터는 업데이트 관련 로직
   */

  /**
   * 입금 처리
   */
  @Transactional
  public double processDeposit(Account account, double amount) {
    return updateAccountBalance(account, amount, true);
  }

  /**
   * 출금 처리
   */
  @Transactional
  public double processWithdrawal(Account account, double amount) throws InvalidWithdrawalAmount {

    // 출금 금액 검증
    validateWithdrawal(account.getBalance(), amount);
    return updateAccountBalance(account, amount, false);
  }


  @Transactional
  public double updateAccountBalance(Account account, double amount, boolean isDeposit) {
    double currentBalance = account.getBalance();
    double afterBalance = isDeposit ? currentBalance + amount : currentBalance - amount;
    account.setBalance(afterBalance);
    account.setUpdatedAt(LocalDateTime.now());
    accountRepository.save(account);
    return afterBalance;
  }


  /**
   * 출금시 유효성 검증
   */
  private void validateWithdrawal(double currentBalance, double amount) {
    if (amount <= 0) {
      throw new InvalidWithdrawalAmount(amount);
    }

    if (amount > currentBalance) {
      throw new InsufficientBalanceException(currentBalance, amount);
    }
  }

  /**
   * 가맹점 Id반환
   */
  private Long getMerchantId(TransactionType transactionType) {

    return switch (transactionType) {
      case D -> 1L;
      case W -> 2L;
      case TD -> 3L;
      case TW -> 4L;
      case CW -> null;
    };
  }
}
