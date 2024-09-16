package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.Account;
import com.goofy.tunabank.v1.domain.Enum.AccountType;
import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import com.goofy.tunabank.v1.domain.Merchant;
import com.goofy.tunabank.v1.domain.TransactionHistory;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionRequestDto;
import com.goofy.tunabank.v1.dto.transaction.response.TransactionResponseDto;
import com.goofy.tunabank.v1.exception.Transaction.InsufficientBalanceException;
import com.goofy.tunabank.v1.exception.Transaction.InvalidMerchantIdException;
import com.goofy.tunabank.v1.exception.Transaction.InvalidWithdrawalAmount;
import com.goofy.tunabank.v1.exception.account.InvalidAccountIdOrTypeException;
import com.goofy.tunabank.v1.mapper.TransactionMapper;
import com.goofy.tunabank.v1.repository.AccountRepository;
import com.goofy.tunabank.v1.repository.MerchantRepository;
import com.goofy.tunabank.v1.repository.TransactionHistoryRepository;
import java.time.LocalDateTime;
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
  public TransactionResponseDto processTransaction(TransactionRequestDto requestDto)
      throws InvalidWithdrawalAmount {

    Long accountId = requestDto.getAccountId();
    AccountType accountType = requestDto.getAccountType();

    Account account = accountRepository.findByIdAndAccountType(accountId, accountType)
        .orElseThrow(() -> new InvalidAccountIdOrTypeException(accountId, accountType));

    TransactionType transactionType = requestDto.getTransactionType();

    double currentBalance = account.getBalance();
    double amount = requestDto.getTransactionBalance();

    //출금 금액 검증
    if (transactionType == TransactionType.W) {
      validateWithdrawal(currentBalance, amount);
    }

    double afterBalance =
        transactionType == TransactionType.D ? currentBalance + amount : currentBalance - amount;

    account.setBalance(afterBalance);
    account.setUpdatedAt(LocalDateTime.now());
    accountRepository.save(account);

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
   * 이체
   */


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
}
