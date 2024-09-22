package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import com.goofy.tunabank.v1.domain.Enum.TransferType;
import com.goofy.tunabank.v1.domain.MoneyBox;
import com.goofy.tunabank.v1.domain.TransactionHistory;
import com.goofy.tunabank.v1.domain.User;
import com.goofy.tunabank.v1.dto.exchange.ExchangeAmountRequestDto;
import com.goofy.tunabank.v1.dto.transaction.TransferDetailDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionHistoryRequestDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionRequestDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransferMBRequestDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransferRequestDto;
import com.goofy.tunabank.v1.dto.transaction.response.TransactionResponseDto;
import com.goofy.tunabank.v1.exception.transaction.InsufficientBalanceException;
import com.goofy.tunabank.v1.exception.transaction.InvalidTransactionTypeException;
import com.goofy.tunabank.v1.exception.transaction.InvalidWithdrawalAmountException;
import com.goofy.tunabank.v1.exception.transaction.MoneyBoxNotFoundException;
import com.goofy.tunabank.v1.exception.transaction.TransactionHistoryNotFoundException;
import com.goofy.tunabank.v1.exception.transaction.UnauthorizedTransactionException;
import com.goofy.tunabank.v1.mapper.TransactionMapper;
import com.goofy.tunabank.v1.repository.MoneyBoxRepository;
import com.goofy.tunabank.v1.repository.transaction.TransactionHistoryRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
  private final ExchangeService exchangeService;
  private final UserService userService;
  private static final int KRW_CURRENCY_ID = 1;

  //TODO: 비밀번호 검증

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

  /**
   * 머니 박스 잔액 업데이트
   */
  @Transactional
  public double updateAccountBalance(MoneyBox moneyBox, double amount, boolean isDeposit) {

    BigDecimal currentBalance = BigDecimal.valueOf(moneyBox.getBalance());
    BigDecimal transactionAmount = BigDecimal.valueOf(amount);

    BigDecimal afterBalance = isDeposit ? currentBalance.add(transactionAmount)
        : currentBalance.subtract(transactionAmount);

    afterBalance = afterBalance.setScale(2, RoundingMode.HALF_UP);

    moneyBox.setBalance(afterBalance.doubleValue());
    moneyBox.setUpdatedAt(LocalDateTime.now());
    moneyBoxRepository.save(moneyBox);

    return afterBalance.doubleValue();
  }


  /**
   * 입금 및 출금(원화, 외화)
   */
  @Transactional
  public TransactionResponseDto processTransaction(TransactionRequestDto requestDto) {

    Long accountId = requestDto.getAccountId();
    int currencyId = requestDto.getCurrencyId();
    MoneyBox moneyBox = findMoneyBoxByAccountAndCurrency(accountId, currencyId);

    //거래 권한 확인
    User user = userService.findUserByUserKey(requestDto.getHeader().getUserKey());
    validateUserAccess(user, moneyBox);

    TransactionType transactionType = requestDto.getTransactionType();
    double amount = requestDto.getTransactionBalance();
    double afterBalance = 0L;

    // 입금 또는 출금 처리
    afterBalance = switch (transactionType) {
      case D -> processDeposit(moneyBox, amount);
      case W -> processWithdrawal(moneyBox, amount);
      default -> throw new InvalidTransactionTypeException(transactionType);
    };

    Long nextId = getNextTransactionId();

    TransactionHistory transactionHistory = TransactionHistory.createTransactionHistory(nextId,
        transactionType, moneyBox, null, requestDto.getHeader().getTransmissionDateTime(), amount,
        afterBalance, requestDto.getTransactionSummary());

    TransactionHistory th = transactionHistoryRepository.save(transactionHistory);
    return transactionMapper.convertTransactionHistoryToTransactionResponseDto(th);
  }

  /**
   * 일반 이체 처리
   */
  @Transactional
  public List<TransactionResponseDto> processGeneralTransfer(TransferRequestDto requestDto) {

    // 출금 머니박스
    MoneyBox withdrawalBox = findMoneyBoxByAccountAndCurrency(requestDto.getWithdrawalAccountId(),
        KRW_CURRENCY_ID);

    //거래 권한 확인
    User user = userService.findUserByUserKey(requestDto.getHeader().getUserKey());
    validateUserAccess(user, withdrawalBox);

    // 입금 머니박스
    MoneyBox depositBox = findMoneyBoxByAccountAndCurrency(requestDto.getDepositAccountId(),
        KRW_CURRENCY_ID);

    TransferDetailDto transferDetailDto = TransferDetailDto.builder()
        .transferType(requestDto.getTransferType()).withdrawalBox(withdrawalBox)
        .withdrawalAmount(requestDto.getTransactionBalance())
        .withdrawalSummary(requestDto.getWithdrawalTransactionSummary()).depositBox(depositBox)
        .depositAmount(requestDto.getTransactionBalance())
        .depositSummary(requestDto.getDepositTransactionSummary())
        .transmissionDateTime(requestDto.getHeader().getTransmissionDateTime()).build();

    return processTransferLogic(transferDetailDto);
  }

  /**
   * 머니박스 이체
   */
  @Transactional
  public List<TransactionResponseDto> processMoneyBoxTransfer(TransferMBRequestDto requestDto) {

    double beforeAmount = requestDto.getTransactionBalance();//해당 머니박스의 통화단위임
    ExchangeAmountRequestDto exchangeAmountRequestDto = switch (requestDto.getSourceCurrencyId()) {
      case 2 ->
          exchangeService.calculateAmountFromForeignCurrencyToKRW(requestDto.getSourceCurrencyId(),
              beforeAmount);// 외화 -> 원화
      default -> {
        // 원화 -> 외화일 때만 10원 단위로 조정
        if (beforeAmount % 10 != 0) {
          beforeAmount -= (beforeAmount % 10);
        }
        yield exchangeService.calculateAmountFromKRWToForeignCurrency(
            requestDto.getTargetCurrencyId(), beforeAmount);// 원화 -> 외화
      }
    };

    double calculatedAmount = exchangeAmountRequestDto.getAmount();
    double exchangeRate = exchangeAmountRequestDto.getExchangeRate();
    String summary = "적용 환율: " + exchangeRate;

    // 출금 머니박스
    MoneyBox withdrawalBox = findMoneyBoxByAccountAndCurrency(requestDto.getAccountId(),
        requestDto.getSourceCurrencyId());

    //거래 권한 확인
    User user = userService.findUserByUserKey(requestDto.getHeader().getUserKey());
    validateUserAccess(user, withdrawalBox);

    // 입금 머니박스
    MoneyBox depositBox = findMoneyBoxByAccountAndCurrency(requestDto.getAccountId(),
        requestDto.getTargetCurrencyId());

    TransferDetailDto transferDetailDto = TransferDetailDto.builder()
        .transferType(requestDto.getTransferType()).withdrawalBox(withdrawalBox)
        .withdrawalAmount(beforeAmount)//신청 금액
        .withdrawalSummary(summary).depositBox(depositBox).depositAmount(calculatedAmount)//환전된 금액
        .depositSummary(summary)
        .transmissionDateTime(requestDto.getHeader().getTransmissionDateTime()).build();

    return processTransferLogic(transferDetailDto);
  }

  /**
   * 이체 공통 로직
   */
  private List<TransactionResponseDto> processTransferLogic(TransferDetailDto dto) {

    // 출금 처리
    double withdrawalAfterBalance = processWithdrawal(dto.getWithdrawalBox(),
        dto.getWithdrawalAmount());

    // 입금 처리
    double depositAfterBalance = processDeposit(dto.getDepositBox(), dto.getDepositAmount());

    Long nextId = getNextTransactionId();

    // 출금 기록 저장
    TransactionType type =
        dto.getTransferType() == TransferType.G ? TransactionType.TW : TransactionType.EW;
    TransactionHistory withdrawalTransactionHistory = TransactionHistory.createTransactionHistory(
        nextId, type, dto.getWithdrawalBox(), dto.getDepositBox().getAccount().getAccountNo(),
        dto.getTransmissionDateTime(), dto.getWithdrawalAmount(), withdrawalAfterBalance,
        dto.getWithdrawalSummary());

    TransactionHistory withdrawalTh = transactionHistoryRepository.save(
        withdrawalTransactionHistory);

    // 입금 기록 저장
    type = dto.getTransferType() == TransferType.G ? TransactionType.TD : TransactionType.ED;
    TransactionHistory depositTransactionHistory = TransactionHistory.createTransactionHistory(
        nextId, type, dto.getDepositBox(), dto.getWithdrawalBox().getAccount().getAccountNo(),
        dto.getTransmissionDateTime(), dto.getDepositAmount(), depositAfterBalance,
        dto.getDepositSummary());

    TransactionHistory depositTh = transactionHistoryRepository.save(depositTransactionHistory);

    // response 변환
    return transactionMapper.convertTransactionHistoriesToResponseDtos(
        List.of(withdrawalTh, depositTh));
  }


  /**
   * 거래 내역 조회
   */
  @Transactional(readOnly = true)
  public List<TransactionResponseDto> getTransactionHistory(
      TransactionHistoryRequestDto requestDto) {

    List<TransactionHistory> transactionHistories = transactionHistoryRepository.findByCustomOrder(
        requestDto).orElseThrow(TransactionHistoryNotFoundException::new);

    return transactionMapper.convertTransactionHistoriesToResponseDtos(transactionHistories);
  }

  /**
   * 다음 거래 기록 id 조회
   */
  @Transactional(readOnly = true)
  public Long getNextTransactionId() {
    Long nextId = transactionHistoryRepository.findMaxAccountId();
    return (nextId == null) ? 1L : nextId + 1;
  }

  /**
   * 머니박스 조회
   */
  @Transactional(readOnly = true)
  public MoneyBox findMoneyBoxByAccountAndCurrency(long accountId, int currencyId) {
    return moneyBoxRepository.findMoneyBoxByAccountAndCurrency(accountId, currencyId)
        .orElseThrow(() -> new MoneyBoxNotFoundException(accountId, currencyId));
  }

  /**
   * 거래 권한 검증
   *
   * @param user     : 거래 요청 user
   * @param moneyBox : 거래 요청 머니박스
   */
  private void validateUserAccess(User user, MoneyBox moneyBox) {
    long userId = user.getUserId();
    long accountId = moneyBox.getAccount().getId(); // 필요한 경우 accountId도 가져옴

    if (userId != moneyBox.getAccount().getUser().getUserId()) {
      throw new UnauthorizedTransactionException(userId, accountId);
    }
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
