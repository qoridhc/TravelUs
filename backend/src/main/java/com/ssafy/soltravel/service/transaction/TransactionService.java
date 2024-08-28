package com.ssafy.soltravel.service.transaction;

import com.ssafy.soltravel.common.Header;
import com.ssafy.soltravel.domain.ForeignAccount;
import com.ssafy.soltravel.domain.GeneralAccount;
import com.ssafy.soltravel.domain.User;
import com.ssafy.soltravel.dto.notification.TransactionNotificationDto;
import com.ssafy.soltravel.dto.transaction.TransactionHistoryDto;
import com.ssafy.soltravel.dto.transaction.request.ForeignTransactionRequestDto;
import com.ssafy.soltravel.dto.transaction.request.TransactionHistoryRequestDto;
import com.ssafy.soltravel.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.dto.transaction.request.TransferRequestDto;
import com.ssafy.soltravel.dto.transaction.response.DepositResponseDto;
import com.ssafy.soltravel.dto.transaction.response.TransferHistoryResponseDto;
import com.ssafy.soltravel.repository.ForeignAccountRepository;
import com.ssafy.soltravel.repository.GeneralAccountRepository;
import com.ssafy.soltravel.repository.UserRepository;
import com.ssafy.soltravel.service.NotificationService;
import com.ssafy.soltravel.util.SecurityUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

  private final Map<String, String> apiKeys;
  private final WebClient webClient;
  private final ModelMapper modelMapper;
  private final UserRepository userRepository;
  private final GeneralAccountRepository generalAccountRepository;

  private final String BASE_URL = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit";
  private final ForeignAccountRepository foreignAccountRepository;
  private final NotificationService notificationService;

  // 일반 계좌 입금
  public ResponseEntity<DepositResponseDto> postAccountDeposit(String accountNo,
      TransactionRequestDto requestDto) {

    Long userId = SecurityUtil.getCurrentUserId();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));

    GeneralAccount generalAccount = generalAccountRepository.findByAccountNo(accountNo)
        .orElseThrow(
            () -> new IllegalArgumentException("The accountNo does not exist: " + accountNo));

    String API_NAME = "updateDemandDepositAccountDeposit";
    String API_URL = BASE_URL + "/" + API_NAME;

    Header header = Header.builder()
        .apiName(API_NAME)
        .apiServiceCode(API_NAME)
        .apiKey(apiKeys.get("API_KEY"))
        .userKey(user.getUserKey())
        .build();

    Map<String, Object> body = new HashMap<>();

    body.put("Header", header);
    body.put("accountNo", accountNo);
    body.put("transactionBalance", requestDto.getTransactionBalance());
    body.put("transactionSummary", requestDto.getTransactionSummary());

    ResponseEntity<Map<String, Object>> response = webClient.post()
        .uri(API_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .retrieve()
        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
        })
        .block();

    Object recObject = response.getBody().get("REC");

    DepositResponseDto depositResponseDto = modelMapper.map(recObject, DepositResponseDto.class);

    Double currentBalance = generalAccount.getBalance();

    generalAccount.setBalance(currentBalance + requestDto.getTransactionBalance());

    generalAccountRepository.save(generalAccount);

    return ResponseEntity.status(HttpStatus.OK).body(depositResponseDto);
  }

  // 일반 계좌 출금
  public ResponseEntity<DepositResponseDto> postAccountWithdrawal(
      String accountNo,
      TransactionRequestDto requestDto) {

    Long userId = SecurityUtil.getCurrentUserId();

    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));

    GeneralAccount generalAccount = generalAccountRepository.findByAccountNo(accountNo)
        .orElseThrow(
            () -> new IllegalArgumentException("The accountNo does not exist: " + accountNo));

    String API_NAME = "updateDemandDepositAccountWithdrawal";
    String API_URL = BASE_URL + "/" + API_NAME;

    Header header = Header.builder()
        .apiName(API_NAME)
        .apiServiceCode(API_NAME)
        .apiKey(apiKeys.get("API_KEY"))
        .userKey(user.getUserKey())
        .build();

    Map<String, Object> body = new HashMap<>();

    body.put("Header", header);
    body.put("accountNo", accountNo);
    body.put("transactionBalance", requestDto.getTransactionBalance());
    body.put("transactionSummary", requestDto.getTransactionSummary());

    ResponseEntity<Map<String, Object>> response = webClient.post()
        .uri(API_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .retrieve()
        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
        })
        .block();

    Object recObject = response.getBody().get("REC");

    DepositResponseDto responseDto = modelMapper.map(recObject, DepositResponseDto.class);

    Double currentBalance = generalAccount.getBalance();
    generalAccount.setBalance(currentBalance - requestDto.getTransactionBalance());

    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

  /**
   * 계좌 이체
   */
  public ResponseEntity<List<TransferHistoryResponseDto>> postAccountTransfer(
      String accountNo,
      TransferRequestDto requestDto
  ) {

    Long userId = SecurityUtil.getCurrentUserId();

    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));

    // 출금할 계좌
    GeneralAccount withDrawalAccount = generalAccountRepository.findByAccountNo(accountNo)
        .orElseThrow(
            () -> new IllegalArgumentException("The accountNo does not exist: " + accountNo));

    // 입금할 계좌
    GeneralAccount depositAccount = generalAccountRepository.findByAccountNo(
            requestDto.getDepositAccountNo())
        .orElseThrow(() -> new IllegalArgumentException(
            "The accountNo does not exist: " + requestDto.getDepositAccountNo()));

    String API_NAME = "updateDemandDepositAccountTransfer";
    String API_URL = BASE_URL + "/" + API_NAME;

    Header header = Header.builder()
        .apiName(API_NAME)
        .apiServiceCode(API_NAME)
        .apiKey(apiKeys.get("API_KEY"))
        .userKey(user.getUserKey())
        .build();

    Map<String, Object> body = new HashMap<>();

    body.put("Header", header);
    body.put("depositAccountNo", requestDto.getDepositAccountNo());
    body.put("depositTransactionSummary", requestDto.getDepositTransactionSummary());
    body.put("transactionBalance", requestDto.getTransactionBalance());
    body.put("withdrawalAccountNo", accountNo);
    body.put("withdrawalTransactionSummary", requestDto.getWithdrawalTransactionSummary());

    ResponseEntity<Map<String, Object>> response = webClient.post()
        .uri(API_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .retrieve()
        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
        })
        .block();

    List<Object> recObject = (List<Object>) response.getBody().get("REC");

    List<TransferHistoryResponseDto> responseDto = recObject.stream()
        .map(value -> modelMapper.map(value, TransferHistoryResponseDto.class))
        .collect(Collectors.toList());

    withDrawalAccount.setBalance(
        withDrawalAccount.getBalance() - requestDto.getTransactionBalance());
    depositAccount.setBalance(depositAccount.getBalance() + requestDto.getTransactionBalance());

    generalAccountRepository.save(withDrawalAccount);
    generalAccountRepository.save(depositAccount);

    //출금 계좌 알림
    TransactionNotificationDto withDrawalNotification = TransactionNotificationDto.builder()
        .userId(userId)
        .accountId(withDrawalAccount.getId())
        .accountNo(withDrawalAccount.getAccountNo())
        .amount(requestDto.getTransactionBalance())
        .balance(withDrawalAccount.getBalance())
        .message("출금 발생").build();
    notificationService.notifyTransactionMessage(withDrawalNotification);


    //입금 계좌 알림
    TransactionNotificationDto depositNotification = TransactionNotificationDto.builder()
        .userId(userId)
        .accountId(depositAccount.getId())
        .accountNo(depositAccount.getAccountNo())
        .amount(requestDto.getTransactionBalance())
        .balance(depositAccount.getBalance())
        .message("입금 발생").build();
    notificationService.notifyTransactionMessage(depositNotification);
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

  public ResponseEntity<List<TransactionHistoryDto>> getHistoryByAccountNo(
      String accountNo,
      TransactionHistoryRequestDto requestDto
  ) {

    Long userId = SecurityUtil.getCurrentUserId();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));

    String API_NAME = "inquireTransactionHistoryList";
    String API_URL = BASE_URL + "/" + API_NAME;

    Header header = Header.builder()
        .apiName(API_NAME)
        .apiServiceCode(API_NAME)
        .apiKey(apiKeys.get("API_KEY"))
        .userKey(user.getUserKey())
        .build();

    Map<String, Object> body = new HashMap<>();

    body.put("Header", header);
    body.put("accountNo", accountNo);
    body.put("startDate", requestDto.getStartDate());
    body.put("endDate", requestDto.getEndDate());
    body.put("transactionType", requestDto.getTransactionType());
    body.put("orderByType", requestDto.getOrderByType());

    ResponseEntity<Map<String, Object>> response = webClient.post()
        .uri(API_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .retrieve()
        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
        })
        .block();

    Map<String, Object> recObject = (Map<String, Object>) response.getBody().get("REC");
    List<Object> recList = (List<Object>) recObject.get("list");

    List<TransactionHistoryDto> responseDto = recList.stream()
        .map(value -> modelMapper.map(value, TransactionHistoryDto.class))
        .collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

  /**
   * 외화 계좌에 입금하는 메서드
   */
  public DepositResponseDto postForeignDeposit(String accountNo,
      ForeignTransactionRequestDto requestDto) {

    Long userId = SecurityUtil.getCurrentUserId();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));

    ForeignAccount foreignAccount = foreignAccountRepository.findByAccountNo(accountNo)
        .orElseThrow(
            () -> new IllegalArgumentException("The AccountNo Does Not Exist" + accountNo));

    String API_NAME = "updateForeignCurrencyDemandDepositAccountDeposit";
    String API_URL = BASE_URL + "/foreignCurrency/" + API_NAME;

    Header header = Header.builder()
        .apiName(API_NAME)
        .apiServiceCode(API_NAME)
        .apiKey(apiKeys.get("API_KEY"))
        .userKey(user.getUserKey())
        .build();

    Map<String, Object> body = new HashMap<>();

    body.put("Header", header);
    body.put("accountNo", accountNo);
    body.put("transactionBalance", requestDto.getTransactionBalance());
    body.put("transactionSummary", requestDto.getTransactionSummary());

    ResponseEntity<Map<String, Object>> response = webClient.post()
        .uri(API_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .retrieve()
        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
        })
        .block();

    Object recObject = response.getBody().get("REC");

    DepositResponseDto depositResponseDto = modelMapper.map(recObject, DepositResponseDto.class);

    Double currentBalance = foreignAccount.getBalance();

    foreignAccount.setBalance(currentBalance + requestDto.getTransactionBalance());

    foreignAccountRepository.save(foreignAccount);

    return depositResponseDto;
  }

  /**
   * 외화 계좌에서 출금하는 메서드
   */
  public DepositResponseDto postForeignWithdrawal(String accountNo,
      ForeignTransactionRequestDto requestDto) {

    Long userId = SecurityUtil.getCurrentUserId();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));

    ForeignAccount foreignAccount = foreignAccountRepository.findByAccountNo(accountNo)
        .orElseThrow(
            () -> new IllegalArgumentException("The AccountNo Does Not Exist" + accountNo));

    String API_NAME = "updateForeignCurrencyDemandDepositAccountWithdrawal";
    String API_URL = BASE_URL + "/foreignCurrency/" + API_NAME;

    Header header = Header.builder()
        .apiName(API_NAME)
        .apiServiceCode(API_NAME)
        .apiKey(apiKeys.get("API_KEY"))
        .userKey(user.getUserKey())
        .build();

    Map<String, Object> body = new HashMap<>();

    body.put("Header", header);
    body.put("accountNo", accountNo);
    body.put("transactionBalance", requestDto.getTransactionBalance());
    body.put("transactionSummary", requestDto.getTransactionSummary());

    ResponseEntity<Map<String, Object>> response = webClient.post()
        .uri(API_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .retrieve()
        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
        })
        .block();

    Object recObject = response.getBody().get("REC");

    DepositResponseDto responseDto = modelMapper.map(recObject, DepositResponseDto.class);

    Double currentBalance = foreignAccount.getBalance();
    foreignAccount.setBalance(currentBalance - requestDto.getTransactionBalance());

    return responseDto;
  }

  /**
   * 외화 통장 거래 내역 조회
   */
  public List<TransactionHistoryDto> getForeignHistoryByAccountNo(String accountNo,
      TransactionHistoryRequestDto requestDto) {

    Long userId = SecurityUtil.getCurrentUserId();
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));

    String API_NAME = "inquireForeignCurrencyTransactionHistoryList";
    String API_URL = BASE_URL + "/foreignCurrency/" + API_NAME;

    Header header = Header.builder()
        .apiName(API_NAME)
        .apiServiceCode(API_NAME)
        .apiKey(apiKeys.get("API_KEY"))
        .userKey(user.getUserKey())
        .build();

    Map<String, Object> body = new HashMap<>();

    body.put("Header", header);
    body.put("accountNo", accountNo);
    body.put("startDate", requestDto.getStartDate());
    body.put("endDate", requestDto.getEndDate());
    body.put("transactionType", requestDto.getTransactionType());
    body.put("orderByType", requestDto.getOrderByType());

    ResponseEntity<Map<String, Object>> response = webClient.post()
        .uri(API_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .retrieve()
        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
        })
        .block();

    Map<String, Object> recObject = (Map<String, Object>) response.getBody().get("REC");
    List<Object> recList = (List<Object>) recObject.get("list");

    List<TransactionHistoryDto> responseDto = recList.stream()
        .map(value -> modelMapper.map(value, TransactionHistoryDto.class))
        .collect(Collectors.toList());

    return responseDto;
  }
}
