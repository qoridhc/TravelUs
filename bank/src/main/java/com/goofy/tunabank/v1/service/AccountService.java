package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.Account;
import com.goofy.tunabank.v1.domain.Bank;
import com.goofy.tunabank.v1.domain.Currency;
import com.goofy.tunabank.v1.domain.Enum.AccountType;
import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.domain.MoneyBox;
import com.goofy.tunabank.v1.domain.User;
import com.goofy.tunabank.v1.dto.ResponseDto;
import com.goofy.tunabank.v1.dto.account.AccountDto;
import com.goofy.tunabank.v1.dto.account.request.AddMoneyBoxRequestDto;
import com.goofy.tunabank.v1.dto.account.request.BalanceRequestDto;
import com.goofy.tunabank.v1.dto.account.request.CreateGeneralAccountRequestDto;
import com.goofy.tunabank.v1.dto.account.request.DeleteAccountRequestDto;
import com.goofy.tunabank.v1.dto.account.request.InquireAccountListRequestDto;
import com.goofy.tunabank.v1.dto.account.request.InquireAccountRequestDto;
import com.goofy.tunabank.v1.dto.account.response.BalanceResponseDto;
import com.goofy.tunabank.v1.dto.moneyBox.MoneyBoxDto;
import com.goofy.tunabank.v1.dto.moneyBox.request.DeleteMoneyBoxRequestDto;
import com.goofy.tunabank.v1.dto.moneyBox.response.DeleteMoneyBoxResponseDto;
import com.goofy.tunabank.v1.dto.transaction.request.TransferMBRequestDto;
import com.goofy.tunabank.v1.exception.account.DuplicateCurrencyException;
import com.goofy.tunabank.v1.exception.account.InvalidAccountNoException;
import com.goofy.tunabank.v1.exception.account.InvalidAccountPasswordException;
import com.goofy.tunabank.v1.exception.account.InvalidBankIdException;
import com.goofy.tunabank.v1.exception.account.InvalidGroupAccountIdException;
import com.goofy.tunabank.v1.exception.account.RefundAccountRequiredException;
import com.goofy.tunabank.v1.exception.account.UserAccountsNotFoundException;
import com.goofy.tunabank.v1.exception.moneybox.MoneyBoxNotFoundException;
import com.goofy.tunabank.v1.mapper.AccountMapper;
import com.goofy.tunabank.v1.mapper.MoneyBoxMapper;
import com.goofy.tunabank.v1.repository.BankRepository;
import com.goofy.tunabank.v1.repository.CurrencyRepository;
import com.goofy.tunabank.v1.repository.MoneyBoxRepository;
import com.goofy.tunabank.v1.repository.account.AccountRepository;
import com.goofy.tunabank.v1.util.LogUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CurrencyRepository currencyRepository;
    private final BankRepository bankRepository;
    private final MoneyBoxRepository moneyBoxRepository;

    private final UserService userService;
    private final TransactionService transactionService;

    private final AccountMapper accountMapper;
    private final MoneyBoxMapper moneyBoxMapper;

    // ==== 계좌 생성 관련 메서드 ====
    public AccountDto postNewAccount(CreateGeneralAccountRequestDto requestDto) {

        // 유저 정보 생성
        User user = userService.findUserByHeader();

        // 일반 계좌 생성
        Currency currency = currencyRepository.findByCurrencyCode(CurrencyType.KRW);

        Bank bank = bankRepository.findById(requestDto.getBankId())
            .orElseThrow(() -> new InvalidBankIdException(requestDto.getBankId()));

        // 계좌 생성
        Account account = Account.createAccount(requestDto, bank, user);

        // KRW 머니박스 기본 생성
        MoneyBox moneyBox = MoneyBox.createMoneyBox(account, currency);

        List<MoneyBox> moneyBoxes = new ArrayList<>();

        moneyBoxes.add(moneyBox);

        account.setMoneyBoxes(moneyBoxes);

        accountRepository.save(account);
        moneyBoxRepository.save(moneyBox);

        // DTO 변환
        AccountDto accountDto = accountMapper.toDto(account);

        List<MoneyBoxDto> moneyBoxDtoList = moneyBoxMapper.toDtoList(account.getMoneyBoxes());
        accountDto.setMoneyBoxDtos(moneyBoxDtoList);

        return accountDto;
    }

    // ==== 유저 계좌 전체 조회 ====
    public List<AccountDto> inqureAccountList(InquireAccountListRequestDto requestDto) {

        LogUtil.info("requestDto", requestDto);

        User user = userService.findUserByHeader();

        List<Account> accountList = accountRepository.findAllAccountsByUserId(user.getUserId())
            .orElseThrow(() -> new UserAccountsNotFoundException(user.getUserId()));

        List<AccountDto> accountDtoList = accountList.stream()
            .filter(account -> {
                if (requestDto.getSearchType().equals("A")) {
                    return true; // "A"인 경우 전체 조회
                }
                return account.getAccountType().name().equals(requestDto.getSearchType()); // "I"나 "G"인 경우 필터링
            })
            .map(accountMapper::toDto)
            .toList();

        return accountDtoList;
    }

    // ==== 계좌 조회 ====
    public AccountDto inquireAccount(InquireAccountRequestDto requestDto) {

        User user = userService.findUserByHeader();

        Account account = accountRepository.findByAccountNo(requestDto.getAccountNo())
            .orElseThrow(() -> new InvalidAccountNoException(requestDto.getAccountNo()));

        // DTO 변환
        AccountDto accountDto = accountMapper.toDto(account);
        accountDto.setCredentialId(account.getUser().getCredentialId());

        List<MoneyBoxDto> moneyBoxDtoList = moneyBoxMapper.toDtoList(account.getMoneyBoxes());
        accountDto.setMoneyBoxDtos(moneyBoxDtoList);

        return accountDto;
    }

    // ==== 계좌 삭제 ====
    public ResponseDto deleteAccount(DeleteAccountRequestDto requestDto) {

        Account account = accountRepository.findByAccountNo(requestDto.getAccountNo())
            .orElseThrow(() -> new InvalidAccountNoException(requestDto.getAccountNo()));

        if (!account.getAccountPassword().equals(requestDto.getAccountPassword())) {
            throw new InvalidAccountPasswordException(requestDto.getAccountPassword());
        }

        // 추후에 자동 환전 & 환불 계좌 로직 추가
        boolean hasBalance = account.getMoneyBoxes().stream().anyMatch(moneyBox -> moneyBox.getBalance() > 0);

        if (hasBalance) {
            throw new RefundAccountRequiredException();
        }

        return new ResponseDto();
    }

    /*
     *  머니박스 관련 메서드
     */

    // ==== 머니 박스 생성 ====
    public List<MoneyBoxDto> addAccountMoneyBox(AddMoneyBoxRequestDto requestDto) {

        // 유저 정보 생성
        User user = userService.findUserByHeader();

        Account account = accountRepository.findByAccountNo(requestDto.getAccountNo())
            .orElseThrow(() -> new InvalidAccountNoException(requestDto.getAccountNo()));

        if (!account.getAccountPassword().equals(requestDto.getAccountPassword())) {
            throw new InvalidAccountPasswordException(requestDto.getAccountPassword());
        }

        if (!account.getAccountType().equals(AccountType.G)) {
            throw new InvalidGroupAccountIdException();
        }

        boolean isExistCurrencyCode = account.getMoneyBoxes().stream()
            .anyMatch(v -> v.getCurrency().getCurrencyCode().equals(requestDto.getCurrencyCode()));

        if (isExistCurrencyCode) {
            throw new DuplicateCurrencyException(requestDto.getCurrencyCode());
        }

        Currency currency = currencyRepository.findByCurrencyCode(requestDto.getCurrencyCode());

        MoneyBox moneyBox = MoneyBox.createMoneyBox(account, currency);

        account.getMoneyBoxes().add(moneyBox);

        accountRepository.save(account);

        return moneyBoxMapper.toDtoList(account.getMoneyBoxes());
    }

    @Transactional
    public DeleteMoneyBoxResponseDto deleteMoneyBox(DeleteMoneyBoxRequestDto requestDto) {

        User user = userService.findUserByHeader();

        Account account = accountRepository.findByAccountNo(requestDto.getAccountNo())
            .orElseThrow(() -> new InvalidAccountNoException(requestDto.getAccountNo()));

        // 머니박스 존재 여부
        MoneyBox moneyBox = account.getMoneyBoxes().stream()
            .filter(box -> box.getCurrency().getCurrencyCode().equals(requestDto.getCurrencyCode()))
            .findFirst()
            .orElseThrow(() -> new MoneyBoxNotFoundException("해당 통화의 MoneyBox가 존재하지 않습니다."));

        // 외화 머니박스 잔액 -> KRW 환전
        MoneyBox krwMoneyBox = account.getMoneyBoxes().get(0);

        double beforeRefundAmount = krwMoneyBox.getBalance();

        if (moneyBox.getBalance() > 0) {
            TransferMBRequestDto dto = TransferMBRequestDto.from(account, moneyBox, requestDto);

            transactionService.processMoneyBoxTransfer(dto);
        }

        moneyBox.closeMoneyBox();

        double afterRefundAmount = krwMoneyBox.getBalance();
        double calcAmount = afterRefundAmount - beforeRefundAmount;

        return new DeleteMoneyBoxResponseDto("CLOSED", account.getAccountNo(), calcAmount);
    }


    public BalanceResponseDto getBalanceByAccountNo(BalanceRequestDto requestDto) {

        MoneyBox moneyBox = transactionService.findMoneyBoxByAccountAndCurrencyCode(requestDto.getAccountNo(),
            requestDto.getCurrencyCode());
        return new BalanceResponseDto(moneyBox.getBalance());
    }
}
