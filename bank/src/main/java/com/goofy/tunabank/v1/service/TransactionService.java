package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.Account;
import com.goofy.tunabank.v1.domain.Enum.AccountType;
import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import com.goofy.tunabank.v1.domain.Merchant;
import com.goofy.tunabank.v1.domain.TransactionHistory;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionRequestDto;
import com.goofy.tunabank.v1.dto.transaction.response.TransactionResponseDto;
import com.goofy.tunabank.v1.exception.Transaction.InvalidMerchantIdException;
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
   * 입금
   */
  @Transactional
  public TransactionResponseDto processTransaction(TransactionRequestDto requestDto) {

    Long accountId = requestDto.getAccountId();
    AccountType accountType = requestDto.getAccountType();

    Account account = accountRepository.findByIdAndAccountType(accountId, accountType)
        .orElseThrow(() -> new InvalidAccountIdOrTypeException(accountId, accountType));

    //Account 잔액 및 수정시간 업데이트
    double currentBalance = account.getBalance();
    double amount=requestDto.getAmount();
    double afterBalance=currentBalance + amount;

    account.setBalance(afterBalance);
    account.setUpdatedAt(LocalDateTime.now());
    accountRepository.save(account);


    // 다음 id값 추출
    Long nextId = transactionHistoryRepository.findMaxAccountId();
    if (nextId == null) {
      nextId = 0L;
    }
    nextId++;

    Merchant merchant= merchantRepository.findById(1L).orElseThrow(() -> new InvalidMerchantIdException(1L));
    
    //TransactionHistory 저장
    TransactionHistory transactionHistory = TransactionHistory.builder()
        .id(nextId)
        .transactionType(TransactionType.D) // 적절한 거래 종류로 변경
        .account(account) // 관련 계좌 객체
        .merchant(merchant) // 관련 가맹점 객체
        .transactionAt(LocalDateTime.now()) // 현재 시각으로 설정
        .amount(amount) // 거래 금액
        .balance(afterBalance) // 업데이트된 잔액
        .build();

    transactionHistoryRepository.save(transactionHistory);

    TransactionResponseDto responseDto = TransactionResponseDto.builder()
        .transactionHistoryId(nextId)
        .transactionType(TransactionType.D)
        .accountNo(account.getAccountNo())
        .transactionAt(LocalDateTime.now())
        .amount(requestDto.getAmount())
        .balance(afterBalance)
        .build();

    return responseDto;
  }
  /**
   * 출금
   */

  /**
   * 이체
   */
}
