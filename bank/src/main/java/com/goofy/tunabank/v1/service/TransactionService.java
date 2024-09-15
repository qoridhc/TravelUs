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

    //TODO:Account 잔액 및 수정시간 업데이트 유효성검사왜안먹냐
    TransactionType transactionType = requestDto.getTransactionType();

    double currentBalance = account.getBalance();
    double amount = requestDto.getAmount();

    if(transactionType==TransactionType.W)
      validateWithdrawal(currentBalance,amount);

    double afterBalance =
        transactionType == TransactionType.D ? currentBalance + amount : currentBalance - amount;

    account.setBalance(afterBalance);
    account.setUpdatedAt(LocalDateTime.now());
    accountRepository.save(account);

    // 다음 id값 추출
    Long nextId = transactionHistoryRepository.findMaxAccountId();
    if (nextId == null) {
      nextId = 0L;
    }
    nextId++;

    long merchantId = transactionType == TransactionType.D ? 1L : 2L;
    Merchant merchant = merchantRepository.findById(merchantId)
        .orElseThrow(() -> new InvalidMerchantIdException(merchantId));

    //TransactionHistory 저장
    TransactionHistory transactionHistory = TransactionHistory.builder()
        .id(nextId)
        .transactionType(transactionType)
        .account(account)
        .merchant(merchant)
        .transactionAt(LocalDateTime.now())
        .amount(amount)
        .balance(afterBalance)
        .build();

    transactionHistoryRepository.save(transactionHistory);

    TransactionResponseDto responseDto = TransactionResponseDto.builder()
        .transactionHistoryId(nextId)
        .transactionType(transactionType)
        .accountNo(account.getAccountNo())
        .transactionAt(LocalDateTime.now())
        .amount(requestDto.getAmount())
        .balance(afterBalance)
        .build();

    return responseDto;
  }
  /**
   * 이체
   */

  private void validateWithdrawal(double currentBalance, double amount)
      throws InvalidWithdrawalAmount {
    if (amount <= 0)
      throw new InvalidWithdrawalAmount(amount);

    if (amount > currentBalance)
      throw new InsufficientBalanceException(currentBalance,amount);
  }
}
