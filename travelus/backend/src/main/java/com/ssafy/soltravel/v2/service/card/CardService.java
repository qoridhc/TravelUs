package com.ssafy.soltravel.v2.service.card;


import com.ssafy.soltravel.v2.common.BankHeader;
import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.dto.card.CardIssueRequestDto;
import com.ssafy.soltravel.v2.dto.card.CardResponseDto;
import com.ssafy.soltravel.v2.exception.UserNotFoundException;
import com.ssafy.soltravel.v2.repository.UserRepository;
import com.ssafy.soltravel.v2.util.LogUtil;
import com.ssafy.soltravel.v2.util.SecurityUtil;
import com.ssafy.soltravel.v2.util.WebClientUtil;
import java.time.LocalDateTime;
import java.util.Map;
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
}
