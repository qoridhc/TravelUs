package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.Account;
import com.goofy.tunabank.v1.domain.Bank;
import com.goofy.tunabank.v1.domain.Currency;
import com.goofy.tunabank.v1.domain.Enum.AccountType;
import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.domain.MoneyBox;
import com.goofy.tunabank.v1.dto.ResponseDto;
import com.goofy.tunabank.v1.dto.account.AccountDto;
import com.goofy.tunabank.v1.dto.account.request.AddMoneyBoxRequestDto;
import com.goofy.tunabank.v1.dto.account.request.CreateGeneralAccountRequestDto;
import com.goofy.tunabank.v1.dto.account.request.InquireAccountRequestDto;
import com.goofy.tunabank.v1.dto.moneyBox.MoneyBoxDto;
import com.goofy.tunabank.v1.exception.account.DuplicateCurrencyException;
import com.goofy.tunabank.v1.exception.account.InvalidAccountIdException;
import com.goofy.tunabank.v1.exception.account.InvalidAccountPasswordException;
import com.goofy.tunabank.v1.exception.account.InvalidBankIdException;
import com.goofy.tunabank.v1.exception.account.InvalidGroupAccountIdException;
import com.goofy.tunabank.v1.exception.account.RefundAccountRequiredException;
import com.goofy.tunabank.v1.mapper.AccountMapper;
import com.goofy.tunabank.v1.mapper.MoneyBoxMapper;
import com.goofy.tunabank.v1.repository.AccountRepository;
import com.goofy.tunabank.v1.repository.BankRepository;
import com.goofy.tunabank.v1.repository.CurrencyRepository;
import com.goofy.tunabank.v1.repository.MoneyBoxRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CurrencyRepository currencyRepository;
    private final BankRepository bankRepository;
    private final MoneyBoxRepository moneyBoxRepository;

    private final AccountMapper accountMapper;
    private final MoneyBoxMapper moneyBoxMapper;

    // ==== 계좌 생성 관련 메서드 ====
    public AccountDto postNewAccount(CreateGeneralAccountRequestDto requestDto) {

        // 일반 계좌 생성
        Currency currency = currencyRepository.findByCurrencyCode(CurrencyType.KRW);

        Bank bank = bankRepository.findById(requestDto.getBankId())
            .orElseThrow(() -> new InvalidBankIdException(requestDto.getBankId()));

        Account generalAccount = Account.builder()
            .bank(bank)
            .accountType(requestDto.getAccountType())
            .accountNo(createAccountNumber(requestDto.getAccountType()))  // 계좌 번호 생성
            .accountPassword(requestDto.getAccountPassword())
            .build();

        accountRepository.save(generalAccount);

        MoneyBox moneyBox = MoneyBox.builder()
            .account(generalAccount)
            .currency(currency).balance(0.0)
            .build();

        moneyBoxRepository.save(moneyBox);

        AccountDto generalAccountDto = accountMapper.toDto(generalAccount);

        List<MoneyBoxDto> moneyBoxDtoList = new ArrayList<>();
        moneyBoxDtoList.add(moneyBoxMapper.toDto(moneyBox));

        generalAccountDto.setMoneyBoxDtos(moneyBoxDtoList);

        return generalAccountDto;
    }

    private String createAccountNumber(AccountType accountType) {

        // 계좌 종류 고유값
        final String code = accountType.getCode();

        // 랜덤 7자리 숫자 생성 (검증 번호는 마지막에 추가)
        String randomPart = String.format("%07d", (int) (Math.random() * 10000000));

        // 검증 번호를 계산하기 위한 계좌 번호 생성
        String accountNumberWithoutCheckDigit = code + randomPart;

        // 검증 번호 계산
        int checkDigit = calculateLuhnCheckDigit(accountNumberWithoutCheckDigit);

        // 지점 번호 -> 209로 고정
        String branchNumber = "209";

        return String.format("%s-%s%d-%s", code, randomPart, checkDigit, branchNumber);
    }

    // Luhn 알고리즘을 사용한 검증 번호 계산
    private int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = false;

        // 오른쪽에서 왼쪽으로 숫자를 처리
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(number.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        // 10으로 나누어 떨어지게 하는 숫자 반환
        return (10 - (sum % 10)) % 10;
    }

    public List<MoneyBoxDto> addAccountMoneyBox(AddMoneyBoxRequestDto requestDto) {

        Account account = accountRepository.findGroupAccountById(requestDto.getAccountId())
            .orElseThrow(() -> new InvalidGroupAccountIdException(requestDto.getAccountId()));

        if (!account.getAccountPassword().equals(requestDto.getAccountPassword())) {
            throw new InvalidAccountPasswordException(requestDto.getAccountPassword());
        }

        boolean isExistCurrencyCode = account.getMoneyBoxes().stream()
            .anyMatch(v -> v.getCurrency().getCurrencyCode().equals(requestDto.getCurrencyCode()));

        if (isExistCurrencyCode) {
            throw new DuplicateCurrencyException(requestDto.getCurrencyCode());
        }

        Currency currency = currencyRepository.findByCurrencyCode(requestDto.getCurrencyCode());

        MoneyBox moneyBox = MoneyBox.builder()
            .account(account)
            .balance(0.0)
            .currency(currency)
            .build();

        moneyBoxRepository.save(moneyBox);

        List<MoneyBox> moneyBoxList = account.getMoneyBoxes();
        moneyBoxList.add(moneyBox);

        return moneyBoxMapper.toDtoList(moneyBoxList);

    }

    // ==== 계좌 조회 ====
    public AccountDto inquireAccount(InquireAccountRequestDto requestDto) {

        Account account = accountRepository.findById(requestDto.getAccountId())
            .orElseThrow(() -> new InvalidAccountIdException(requestDto.getAccountId()));

        if (!account.getAccountPassword().equals(requestDto.getAccountPassword())) {
            throw new InvalidAccountPasswordException(requestDto.getAccountPassword());
        }

        AccountDto accountDto = accountMapper.toDto(account);

        List<MoneyBox> moneyBoxDtoList = account.getMoneyBoxes();
        accountDto.setMoneyBoxDtos(moneyBoxMapper.toDtoList(moneyBoxDtoList));

        return accountDto;
    }

    // ==== 계좌 삭제 ====
    public ResponseDto deleteAccount(InquireAccountRequestDto requestDto) {

        Account account = accountRepository.findById(requestDto.getAccountId())
            .orElseThrow(() -> new InvalidAccountIdException(requestDto.getAccountId()));

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
}
