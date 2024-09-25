package com.ssafy.soltravel.v2.service.card;


import com.ssafy.soltravel.v2.common.BankHeader;
import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.dto.account.AccountDto;
import com.ssafy.soltravel.v2.dto.account.request.InquireAccountRequestDto;
import com.ssafy.soltravel.v2.dto.card.CardIssueRequestDto;
import com.ssafy.soltravel.v2.dto.card.CardListRequestDto;
import com.ssafy.soltravel.v2.dto.card.CardPaymentRequestDto;
import com.ssafy.soltravel.v2.dto.card.CardPaymentResponseDto;
import com.ssafy.soltravel.v2.dto.card.CardRequestDto;
import com.ssafy.soltravel.v2.dto.card.CardResponseDto;
import com.ssafy.soltravel.v2.dto.moneyBox.MoneyBoxDto;
import com.ssafy.soltravel.v2.exception.moneybox.MoneyBoxNotFoundException;
import com.ssafy.soltravel.v2.repository.UserRepository;
import com.ssafy.soltravel.v2.service.account.AccountService;
import com.ssafy.soltravel.v2.util.LogUtil;
import com.ssafy.soltravel.v2.util.SecurityUtil;
import com.ssafy.soltravel.v2.util.WebClientUtil;
import java.math.BigDecimal;
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
    private final Map<String, String> apiKeys;
    private final AccountService accountService;
    private final UserRepository userRepository;
    private final WebClientUtil webClientUtil;
    private final SecurityUtil securityUtil;

    /*
     * 카드 발급
     */
    public CardResponseDto createNewCard(CardIssueRequestDto request) {

        // 유저 조회
        User user = securityUtil.getUserByToken();

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
        User user = securityUtil.getUserByToken();

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
            DEFAULT_REQUEST_URI + "/list", request, CardListRequestDto.class
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

        // 유저 조회
        User user = securityUtil.getUserByToken();

        // 카드 조회
        CardResponseDto card = findCard(request.getCardNo(), user.getUserKey());

        // 통잔 잔액 조회
        Double beforeBalance = getBalance(
            card.getWithdrawalAccountNo(),
            request.getCurrencyCode()
        );

        request.setTransactionId(UUID.randomUUID().toString());
        request.setHeader(
            BankHeader.createHeader(
                apiKeys.get("API_KEY"),
                user.getUserKey()
            )
        );

        ResponseEntity<Map<String, Object>> response = webClientUtil.request(
            DEFAULT_REQUEST_URI + "/payment", request, CardPaymentRequestDto.class
        );

        Map<String, Object> recObject = (Map<String, Object>) response.getBody().get("REC");
        CardPaymentResponseDto respponse = toPaymentDto(recObject);

        // 통장 잔액 조회
        Double afterBalance = getBalance(
            card.getWithdrawalAccountNo(),
            request.getCurrencyCode()
        );

        // 결제 금액 확인
        BigDecimal before = BigDecimal.valueOf(beforeBalance);
        BigDecimal after = BigDecimal.valueOf(afterBalance);
        if (after.subtract(before).doubleValue() != request.getPaymentBalance()) {

        }

        return toPaymentDto(recObject);
    }


    /*
     * 특정 통화 코드 통잔 잔액 조회
     */
    private Double getBalance(String accountNo, String currencyCode) {

        InquireAccountRequestDto inquireDto = InquireAccountRequestDto.builder()
            .accountNo(accountNo)
            .build();

        AccountDto account = accountService.getByAccountNo(inquireDto);
        MoneyBoxDto moneyBox = account.getMoneyBoxDtos().stream()
            .filter(mb -> mb.getCurrencyCode().equals(CurrencyType.valueOf(currencyCode)))
            .findFirst()
            .orElseThrow(() -> new MoneyBoxNotFoundException(currencyCode));

        return moneyBox.getBalance();
    }


    /*
     * 카드 단건 조회
     */
    private CardResponseDto findCard(String cardNo, String userKey) {

        // body 셋팅
        CardRequestDto requestDto = CardRequestDto.builder()
            .cardNo(cardNo)
            .header(
                BankHeader.createHeader(apiKeys.get("API_KEY"), userKey)
            )
            .build();

        // 요청
        ResponseEntity<Map<String, Object>> response = webClientUtil.request(
            DEFAULT_REQUEST_URI + "/search", requestDto, CardRequestDto.class
        );

        Map<String, Object> recObject = (Map<String, Object>) response.getBody().get("REC");
        return toDto(recObject);
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
