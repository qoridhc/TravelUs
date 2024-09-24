package com.ssafy.soltravel.v2.service.card;


import com.ssafy.soltravel.v2.common.BankHeader;
import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.dto.card.CardIssueRequestDto;
import com.ssafy.soltravel.v2.dto.card.CardListRequestDto;
import com.ssafy.soltravel.v2.dto.card.CardPaymentRequestDto;
import com.ssafy.soltravel.v2.dto.card.CardPaymentResponseDto;
import com.ssafy.soltravel.v2.dto.card.CardResponseDto;
import com.ssafy.soltravel.v2.dto.transaction.response.TransferHistoryResponseDto;
import com.ssafy.soltravel.v2.exception.UserNotFoundException;
import com.ssafy.soltravel.v2.mapper.CardMapper;
import com.ssafy.soltravel.v2.repository.UserRepository;
import com.ssafy.soltravel.v2.util.LogUtil;
import com.ssafy.soltravel.v2.util.SecurityUtil;
import com.ssafy.soltravel.v2.util.WebClientUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CardService {

  private static final String DEFAULT_REQUEST_URI = "/card";

  private final WebClientUtil webClientUtil;
  private final Map<String, String> apiKeys;
  private final UserRepository userRepository;

  /*
  * 카드 발급
  */
  public CardResponseDto createNewCard(CardIssueRequestDto request) {

    // 유저 조회
    Long userId = SecurityUtil.getCurrentUserId();
    User user = userRepository.findByUserId(userId).orElseThrow(
        () -> new UserNotFoundException(userId)
    );

    // 요청용 body 수정
    request.setHeader(
        BankHeader.createHeader(
            apiKeys.get("API_KEY"),
            user.getUserKey()
        )
    );

    // 은행 요청
    LogUtil.info("은행 요청", request);
    String requestUri = String.format("%s/issue", DEFAULT_REQUEST_URI);
    ResponseEntity<Map<String, Object>> response = webClientUtil.request(
        requestUri, request, CardIssueRequestDto.class
    );

    // 응답
    response.getBody().get("");
    return toDto(response.getBody());
  }

  /*
  * 카드 조회
  */
  public List<CardResponseDto> findAllCards() {

    // 유저 조회
    Long userId = SecurityUtil.getCurrentUserId();
    User user = userRepository.findByUserId(userId).orElseThrow(
        () -> new UserNotFoundException(userId)
    );

    // api 요청 바디 셋팅
    CardListRequestDto request = CardListRequestDto.builder()
        .header(
            BankHeader.createHeader(
                apiKeys.get("API_KEY"),
                user.getUserKey()
            )
        )
        .build();

    LogUtil.info("은행 요청", request);
    ResponseEntity<Map<String, Object>> response = webClientUtil.request(
        DEFAULT_REQUEST_URI+"/list", request, CardListRequestDto.class
    );

    List<Map<String, Object>> recObject = (List<Map<String, Object>>) response.getBody().get("REC");

    return recObject.stream()
        .map(value -> toDto(value))
        .collect(Collectors.toList());
  }

  /*
  * 결제
  */
  public CardPaymentResponseDto makeCardPayment(CardPaymentRequestDto request) {

    Long userId = SecurityUtil.getCurrentUserId();
    User user = userRepository.findByUserId(userId).orElseThrow(
        () -> new UserNotFoundException(userId)
    );

    request.setTransactionId(UUID.randomUUID().toString());
    request.setHeader(
        BankHeader.createHeader(
            apiKeys.get("API_KEY"),
            user.getUserKey()
        )
    );

    ResponseEntity<Map<String, Object>> response = webClientUtil.request(
        DEFAULT_REQUEST_URI+"/payment", request, CardPaymentRequestDto.class
    );

    Map<String, Object> recObject = (Map<String, Object>) response.getBody().get("REC");
    return toPaymentDto(recObject);
  }

  private CardResponseDto toDto(Map<String, Object> response) {
    CardResponseDto dto = CardResponseDto.builder()
        .cardNo(response.get("cardNo").toString())
        .cvc(response.get("cvc").toString())
        .cardUniqueNo(response.get("cardUniqueNo").toString())
        .cardIssuerName(response.get("cardIssuerName").toString())
        .cardName(response.get("cardName").toString())
        .cardDescription(response.get("cardDescription").toString())
        .cardExpiryDate(response.get("cardExpiryDate").toString())
        .withdrawalAccountNo(response.get("withdrawalAccountNo").toString())
        .build();

    return dto;
  }

  private CardPaymentResponseDto toPaymentDto(Map<String, Object> response) {
    CardPaymentResponseDto dto = CardPaymentResponseDto.builder()
        .merchantId(response.get("merchantId").toString())
        .merchantName(response.get("merchantName").toString())
        .category(response.get("category").toString())
        .paymentAt(response.get("paymentAt").toString())
        .currencyCode(response.get("currencyCode").toString())
        .paymentBalance(response.get("paymentBalance").toString())
        .build();

    return dto;
  }
}
