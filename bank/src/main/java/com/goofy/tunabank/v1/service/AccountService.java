package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.Account;
import com.goofy.tunabank.v1.domain.Bank;
import com.goofy.tunabank.v1.domain.Currency;
import com.goofy.tunabank.v1.domain.Enum.CurrencyType;
import com.goofy.tunabank.v1.domain.MoneyBox;
import com.goofy.tunabank.v1.domain.User;
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

    private final UserService userService;

    private final AccountMapper accountMapper;
    private final MoneyBoxMapper moneyBoxMapper;

    // ==== 계좌 생성 관련 메서드 ====
    public AccountDto postNewAccount(CreateGeneralAccountRequestDto requestDto) {

        // 유저 정보 생성
        User user = userService.findUserByUserKey(requestDto.getHeader().getUserKey());

        // 일반 계좌 생성
        Currency currency = currencyRepository.findByCurrencyCode(CurrencyType.KRW);

        Bank bank = bankRepository.findById(requestDto.getBankId())
            .orElseThrow(() -> new InvalidBankIdException(requestDto.getBankId()));

        // 계좌 및 MoneyBox 생성
        Account account = Account.createAccount(requestDto, bank, user, currency);

        accountRepository.save(account);

        // DTO 변환
        AccountDto accountDto = accountMapper.toDto(account);

        List<MoneyBoxDto> moneyBoxDtoList = moneyBoxMapper.toDtoList(account.getMoneyBoxes());
        accountDto.setMoneyBoxDtos(moneyBoxDtoList);

        return accountDto;
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
