package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.Account;
import com.goofy.tunabank.v1.domain.Card;
import com.goofy.tunabank.v1.domain.CardHistory;
import com.goofy.tunabank.v1.domain.CardProduct;
import com.goofy.tunabank.v1.domain.MoneyBox;
import com.goofy.tunabank.v1.domain.User;
import com.goofy.tunabank.v1.dto.card.CardIssueRequestDto;
import com.goofy.tunabank.v1.dto.card.CardIssueResponseDto;
import com.goofy.tunabank.v1.dto.card.CardListRequestDto;
import com.goofy.tunabank.v1.dto.card.CardListResponseDto;
import com.goofy.tunabank.v1.dto.card.CardPaymentRequestDto;
import com.goofy.tunabank.v1.dto.card.CardPaymentResponseDto;
import com.goofy.tunabank.v1.exception.account.InvalidAccountNoException;
import com.goofy.tunabank.v1.exception.card.CardExpiredException;
import com.goofy.tunabank.v1.exception.card.CardNotFoundException;
import com.goofy.tunabank.v1.exception.card.CardOwnershipException;
import com.goofy.tunabank.v1.exception.card.CardProductNotFoundException;
import com.goofy.tunabank.v1.exception.card.DuplicateTransactionIdException;
import com.goofy.tunabank.v1.exception.card.InvalidCvcException;
import com.goofy.tunabank.v1.exception.moneybox.MoneyBoxNotFoundException;
import com.goofy.tunabank.v1.mapper.CardMapper;
import com.goofy.tunabank.v1.repository.AccountRepository;
import com.goofy.tunabank.v1.repository.CardHistoryRepository;
import com.goofy.tunabank.v1.repository.CardProductRepository;
import com.goofy.tunabank.v1.repository.CardRepository;
import com.goofy.tunabank.v1.repository.MoneyBoxRepository;
import com.goofy.tunabank.v1.repository.UserRepository;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CardService {

  private final UserService userService;
  private final UserRepository userRepository;

  private final CardRepository cardRepository;
  private final AccountRepository accountRepository;
  private final CardProductRepository cardProductRepository;

  private static final String DEFAULT_ISSUER_NAME = "Tuna Bank";
  private static final SecureRandom random = new SecureRandom();
  private final CardHistoryRepository cardHistoryRepository;
  private final MoneyBoxRepository moneyBoxRepository;

  /*
  * 카드 신규 발급
  */
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

    // 카드 생성(카드 번호, cvc 번호 생성) 및 저장
    Card card = Card.createCard(account, cardProduct, generateCardNumber(), generateCvc());
    cardRepository.save(card);

    return CardMapper.INSTANCE.cardToCardIssueResponseDto(
        card,
        cardProduct,
        accountNo,
        DEFAULT_ISSUER_NAME
    );
  }


  /*
  * 카드 목록 조회
  */
  public List<CardListResponseDto> findAllCards(CardListRequestDto request) {

    // 유저 조회
    User user = userService.findUserByHeader();

    // 유저에 해당하는 통장/ 카드를 조인해서 조회
    List<Card> cards = cardRepository.findByUser(user);

    // DTO로 변환해서 return
    return cards.stream()
        .map(card -> CardMapper.INSTANCE.cardToCardListResponseDto(card, DEFAULT_ISSUER_NAME))
        .collect(Collectors.toList());
  }


  /*
  * 결제
  */
  public CardPaymentResponseDto makeCardPayment(CardPaymentRequestDto request) {

    // 카드 조회
    Card card = cardRepository.findByCardNo(request.getCardNo()).orElseThrow(
        () -> new CardNotFoundException(request.getCardNo())
    );

    // 카드 정보 일치 검증(유저 소유 여부)
    User user = userService.findUserByHeader();
    User cardUser = card.getAccount().getUser();
    if(user != cardUser) {
      throw new CardOwnershipException(request.getCardNo());
    }

    // 카드 유효성 검증
    if(card.getCvc() != request.getCvc()) {
      throw new InvalidCvcException(request.getCvc());
    }

    if(card.getExpireAt().isBefore(LocalDateTime.now())) {
      throw new CardExpiredException(request.getCardNo());
    }

    // 결제 유효성 검증
    List<CardHistory> history = cardHistoryRepository.findByTransactionId(request.getTransactionId()).orElse(
        Collections.emptyList()
    );
    if(!history.isEmpty()) {
      throw new DuplicateTransactionIdException(request.getTransactionId());
    }

    // 카드 잔액 확인
    MoneyBox money = moneyBoxRepository.findMoneyBoxByAccountAndCurrency(
        card.getAccount().getId(),
        request.getCurrencyId()
    ).orElseThrow(
        () -> new MoneyBoxNotFoundException(request.getCurrencyId())
    );
    
    // 현재 환율 확인


    //  결제


    return null;
  }


  /*
  * 카드 번호 생성
  */
  private String generateCardNumber() {
    String bin = "400000";  // 예: Visa의 경우 일반적으로 4로 시작
    String accountNumber = String.format("%09d", random.nextInt(1000000000));
    String cardNumberWithoutCheckDigit = bin + accountNumber;
    String checkDigit = calculateLuhnCheckDigit(cardNumberWithoutCheckDigit);
    return cardNumberWithoutCheckDigit + checkDigit;
  }

  /*
  * 카드번호 가장 뒷자리, 룬 체크 알고리즘
  */
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

  /*
  * CVC 생성
  */
  private String generateCvc() {
    return String.format("%03d", random.nextInt(1000));
  }
}
