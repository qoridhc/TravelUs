package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.Account;
import com.goofy.tunabank.v1.domain.Card;
import com.goofy.tunabank.v1.domain.CardProduct;
import com.goofy.tunabank.v1.domain.User;
import com.goofy.tunabank.v1.dto.card.CardIssueRequestDto;
import com.goofy.tunabank.v1.dto.card.CardIssueResponseDto;
import com.goofy.tunabank.v1.exception.account.InvalidAccountIdException;
import com.goofy.tunabank.v1.exception.account.InvalidAccountNoException;
import com.goofy.tunabank.v1.exception.card.CardProductNotFoundException;
import com.goofy.tunabank.v1.repository.AccountRepository;
import com.goofy.tunabank.v1.repository.CardProductRepository;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CardService {

  private final UserService userService;
  private final AccountRepository accountRepository;
  private final CardProductRepository cardProductRepository;

  private static final SecureRandom random = new SecureRandom();

  public CardIssueResponseDto createNewCard(CardIssueRequestDto request) {

    // 연결 계좌 검증
    String accountNo = request.getWithdrawalAccountNo();
    Account account = accountRepository.findByAccountNo(accountNo).orElseThrow(
        ()-> new InvalidAccountNoException(accountNo)
    );

    // 카드 상품 선택
    String uniqueNo = request.getCardUniqueNo();
    CardProduct cardProduct = cardProductRepository.findByUniqueNo(uniqueNo).orElseThrow(
        () -> new CardProductNotFoundException(uniqueNo)
    );

    // 카드 생성 로직 구현
    //1. 카드 번호 생성
    //2. cvc 생성
    String cardNo = generateCardNumber();
    String cvc = generateCvc();
    Card card = Card.createCard(account, cardProduct, cardNo, cvc);

    //save

    //response
    return null;
  }

  private String generateCardNumber() {
    String bin = "400000";  // 예: Visa의 경우 일반적으로 4로 시작
    String accountNumber = String.format("%09d", random.nextInt(1000000000));
    String cardNumberWithoutCheckDigit = bin + accountNumber;
    String checkDigit = calculateLuhnCheckDigit(cardNumberWithoutCheckDigit);
    return cardNumberWithoutCheckDigit + checkDigit;
  }

  private String calculateLuhnCheckDigit(String number) {
    int sum = 0;
    boolean alternate = false;
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
    return Integer.toString((10 - (sum % 10)) % 10);
  }

  private String generateCvc() {
    return String.format("%03d", random.nextInt(1000));
  }
}
