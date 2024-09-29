package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import com.goofy.tunabank.v1.domain.Enum.TransferType;
import com.goofy.tunabank.v1.domain.MoneyBox;
import com.goofy.tunabank.v1.domain.User;
import com.goofy.tunabank.v1.domain.history.AbstractHistory;
import com.goofy.tunabank.v1.domain.history.HistoryId;
import com.goofy.tunabank.v1.domain.history.TransactionHistory;
import com.goofy.tunabank.v1.dto.exchange.ExchangeAmountRequestDto;
import com.goofy.tunabank.v1.dto.transaction.TransferDetailDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionHistoryListRequestDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionHistoryRequestDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionRequestDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransferMBRequestDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransferRequestDto;
import com.goofy.tunabank.v1.dto.transaction.response.HistoryResponseDto;
import com.goofy.tunabank.v1.dto.transaction.response.TransactionResponseDto;
import com.goofy.tunabank.v1.exception.account.InvalidAccountNoException;
import com.goofy.tunabank.v1.exception.account.InvalidAccountPasswordException;
import com.goofy.tunabank.v1.exception.transaction.InsufficientBalanceException;
import com.goofy.tunabank.v1.exception.transaction.InvalidTransactionTypeException;
import com.goofy.tunabank.v1.exception.transaction.InvalidWithdrawalAmountException;
import com.goofy.tunabank.v1.exception.transaction.MoneyBoxNotFoundException;
import com.goofy.tunabank.v1.exception.transaction.TransactionHistoryNotFoundException;
import com.goofy.tunabank.v1.exception.transaction.UnauthorizedTransactionException;
import com.goofy.tunabank.v1.mapper.HistoryMapper;
import com.goofy.tunabank.v1.mapper.TransactionMapper;
import com.goofy.tunabank.v1.repository.AbstractHistoryRepository;
import com.goofy.tunabank.v1.repository.MoneyBoxRepository;
import com.goofy.tunabank.v1.repository.account.AccountRepository;
import com.goofy.tunabank.v1.repository.transaction.TransactionHistoryRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

  private final TransactionHistoryRepository transactionHistoryRepository;
  private final AbstractHistoryRepository abstractHistoryRepository;
  private final MoneyBoxRepository moneyBoxRepository;
  private final TransactionMapper transactionMapper;
  private final HistoryMapper historyMapper;
  private final ExchangeService exchangeService;
  private final UserService userService;
  private final AccountRepository accountRepository;


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

    String accountNo = requestDto.getAccountNo();
    CurrencyType currencyCode = requestDto.getCurrencyCode();
    MoneyBox moneyBox = findMoneyBoxByAccountAndCurrencyCode(accountNo, currencyCode);

    //거래 권한 확인
    User user = userService.findUserByHeader();
    validateUserAccess(user, moneyBox);

    //비밀번호 검증
    validatePassword(requestDto.getAccountPassword(), accountNo);

    TransactionType transactionType = requestDto.getTransactionType();
    double amount = requestDto.getTransactionBalance();
    double afterBalance = 0L;

    // 입금 또는 출금 처리
    afterBalance = switch (transactionType) {
      case D, SD -> processDeposit(moneyBox, amount);
      case W, SW -> processWithdrawal(moneyBox, amount);
      default -> throw new InvalidTransactionTypeException(transactionType);
    };

    Long nextId = getNextTransactionId();

    TransactionHistory transactionHistory = TransactionHistory.createTransactionHistory(nextId,
        transactionType, moneyBox, null, requestDto.getHeader().getTransmissionDateTime(), amount,
        afterBalance, requestDto.getTransactionSummary());

    TransactionHistory th = transactionHistoryRepository.save(transactionHistory);
    return transactionMapper.toTransactionResponseDto(th);
  }

  /**
   * 일반 이체 처리
   */
  @Transactional
  public List<TransactionResponseDto> processGeneralTransfer(TransferRequestDto requestDto) {

    // 출금 머니박스
    MoneyBox withdrawalBox = findMoneyBoxByAccountAndCurrencyCode(
        requestDto.getWithdrawalAccountNo(),
        CurrencyType.KRW);

    //거래 권한 확인
    User user = userService.findUserByHeader();
    validateUserAccess(user, withdrawalBox);

    //비밀번호 검증
    validatePassword(requestDto.getAccountPassword(), requestDto.getWithdrawalAccountNo());

    // 입금 머니박스
    MoneyBox depositBox = findMoneyBoxByAccountAndCurrencyCode(requestDto.getDepositAccountNo(),
        CurrencyType.KRW);

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
   * 자동환전
   */
  public List<TransactionResponseDto> processAutoExchange(TransferMBRequestDto requestDto) {

    requestDto.setAccountPassword(getPassword(requestDto.getAccountNo()));
    return processMoneyBoxTransfer(requestDto);
  }

  /**
   * 정산 입출금
   */
  public TransactionResponseDto processAutoSettlement(TransactionRequestDto requestDto) {

    requestDto.setAccountPassword(getPassword(requestDto.getAccountNo()));
    return processTransaction(requestDto);
  }


  /**
   * 머니박스 이체(환전)
   */
  @Transactional
  public List<TransactionResponseDto> processMoneyBoxTransfer(TransferMBRequestDto requestDto) {

    double beforeAmount = requestDto.getTransactionBalance();//해당 머니박스의 통화단위임
    String summary = "";
    ExchangeAmountRequestDto exchangeAmountRequestDto = switch (requestDto.getSourceCurrencyCode()) {
      case USD -> {
        summary = "재환전";
        yield exchangeService.calculateAmountFromForeignCurrencyToKRW(
            requestDto.getSourceCurrencyCode(),
            beforeAmount); // 외화 -> 원화
      }
      default -> {
        summary = "환전";
        // 원화 -> 외화일 때만 10원 단위로 조정
        if (beforeAmount % 10 != 0) {
          beforeAmount -= (beforeAmount % 10);
        }
        yield exchangeService.calculateAmountFromKRWToForeignCurrency(
            requestDto.getTargetCurrencyCode(), beforeAmount); // 원화 -> 외화
      }
    };

    double calculatedAmount = exchangeAmountRequestDto.getAmount();
    double exchangeRate = exchangeAmountRequestDto.getExchangeRate();
    summary += (", 적용 환율: " + exchangeRate);

    // 출금 머니박스
    MoneyBox withdrawalBox = findMoneyBoxByAccountAndCurrencyCode(requestDto.getAccountNo(),
        requestDto.getSourceCurrencyCode());

    //거래 권한 확인
    User user = userService.findUserByHeader();
    validateUserAccess(user, withdrawalBox);

    //비밀번호 검증
    validatePassword(requestDto.getAccountPassword(), requestDto.getAccountNo());

    // 입금 머니박스
    MoneyBox depositBox = findMoneyBoxByAccountAndCurrencyCode(requestDto.getAccountNo(),
        requestDto.getTargetCurrencyCode());

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
    return transactionMapper.toTransactionResponseDtos(
        List.of(withdrawalTh, depositTh));
  }

  /**
   * 거래 내역 목록 조회
   */
  @Transactional(readOnly = true)
  public Page<HistoryResponseDto> getTransactionHistory(TransactionHistoryListRequestDto requestDto) {

    // page와 size가 null일 경우 Pageable을 null로 설정
    Pageable pageable = (requestDto.getPage() != null && requestDto.getSize() != null)
        ? PageRequest.of(requestDto.getPage(), requestDto.getSize())
        : null;

    // 거래 기록 조회
    Page<AbstractHistory> transactionHistories = transactionHistoryRepository.findHistoryByAccountNo(
            requestDto, pageable) // pageable이 null일 경우 전체 데이터를 조회
        .orElseThrow(TransactionHistoryNotFoundException::new);

    return transactionHistories.map(historyMapper::toHistoryResponseDto);
  }



  /**
   * 거래 내역 단건 조회
   */
  @Transactional(readOnly = true)
  public HistoryResponseDto getHistory(
      TransactionHistoryRequestDto requestDto) {

    TransactionHistory transactionHistory = transactionHistoryRepository.findById(
        new HistoryId(requestDto.getTransactionHistoryId(),
            requestDto.getTransactionType())).orElseThrow(TransactionHistoryNotFoundException::new);

    return historyMapper.toHistoryResponseDto(transactionHistory);
  }

  /**
   * 다음 거래 기록 id 조회
   */
  @Transactional(readOnly = true)
  public Long getNextTransactionId() {
    Long nextId = abstractHistoryRepository.findMaxHistoryId();
    return (nextId == null) ? 1L : nextId + 1;
  }

  /**
   * 머니박스 조회
   */
  @Transactional(readOnly = true)
  public MoneyBox findMoneyBoxByAccountAndCurrencyCode(String accountNo, CurrencyType
      currencyCode) {
    return moneyBoxRepository.findMoneyBoxByAccountNoAndCurrency(accountNo, currencyCode)
        .orElseThrow(() -> new MoneyBoxNotFoundException(accountNo, currencyCode));
  }

  /**
   * 거래 권한 검증
   *
   * @param user     : 거래 요청 user
   * @param moneyBox : 거래 요청 머니박스
   */
  private void validateUserAccess(User user, MoneyBox moneyBox) {
    long userId = user.getUserId();
    long accountId = moneyBox.getAccount().getId();

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

  /**
   * 비밀번호 검증
   */
  private boolean validatePassword(String password, String accountNo) {

    String accountPassword = getPassword(accountNo);

    if (!password.equals(accountPassword)) {
      throw new InvalidAccountPasswordException(password);
    }
    return true;
  }

  private String getPassword(String accountNo) {

    return accountRepository.findPasswordByAccountNo(accountNo)
        .orElseThrow(() -> new InvalidAccountNoException(accountNo));
  }
}
