package com.ssafy.soltravel.v2.service.transaction;

import com.ssafy.soltravel.v2.common.Header;
import com.ssafy.soltravel.v2.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransferRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.response.TransactionResponseDto;
import com.ssafy.soltravel.v2.dto.transaction.response.TransferHistoryResponseDto;
import com.ssafy.soltravel.v2.repository.GeneralAccountRepository;
import com.ssafy.soltravel.v2.repository.UserRepository;
import com.ssafy.soltravel.v2.util.WebClientUtil;
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

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

  private final Map<String, String> apiKeys;
  private final WebClientUtil webClientUtil;
  private final ModelMapper modelMapper;
  private final UserRepository userRepository;
  private final GeneralAccountRepository generalAccountRepository;

  private final String BASE_URL = "http://localhost:8080/api/v1/bank/transaction";

  // 입금
  public ResponseEntity<TransactionResponseDto> postAccountDeposit(TransactionRequestDto requestDto) {

    return processTransaction("deposit", requestDto);
  }

  // 출금
  public ResponseEntity<TransactionResponseDto> postAccountWithdrawal(
      TransactionRequestDto requestDto) {

    return processTransaction("withdrawal", requestDto);
  }


  //입출금 공통 요청 처리 메서드
  private ResponseEntity<TransactionResponseDto> processTransaction(String apiName,
      TransactionRequestDto requestDto) {

//    Long userId = SecurityUtil.getCurrentUserId();
//    User user = userRepository.findByUserId(userId)
//        .orElseThrow(() -> new UserNotFoundException(userId));

    String API_URL = BASE_URL + "/" + apiName;

    Header header = Header.builder()
//        .apiKey(apiKeys.get("API_KEY"))
//        .userKey(user.getUserKey()).build();
        .apiKey("V4BYi-78qMmIyXoJOcnCXRE0TXIyWgjBlZcAYe4JIljMu6of_GJ8kbUBWlfqW0WN")
        .userKey("779e4e7e-ccd7-420c-92b5-8770cfd9199e").build();

    Map<String, Object> body = new HashMap<>();
    body.put("Header", header);
    body.put("accountId", requestDto.getAccountId());
    body.put("currencyId", requestDto.getCurrencyId());
    body.put("transactionType", requestDto.getTransactionType());
    body.put("transactionBalance", requestDto.getTransactionBalance());
    body.put("transactionSummary", requestDto.getTransactionSummary());

    ResponseEntity<Map<String, Object>> response = webClientUtil.request(API_URL, body, Map.class);

    Object recObject = response.getBody().get("REC");

    TransactionResponseDto transactionResponseDto = modelMapper.map(recObject, TransactionResponseDto.class);
    return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDto);
  }

  /**
   * 계좌 이체
   */
  public ResponseEntity<List<TransferHistoryResponseDto>> postAccountTransfer(
      TransferRequestDto requestDto) {


//    String API_NAME = "general";
//    String API_URL = BASE_URL + "/" + API_NAME;
//
//    Header header = Header.builder().apiName(API_NAME).apiServiceCode(API_NAME)
//        .apiKey(apiKeys.get("API_KEY")).userKey(user.getUserKey()).build();
//
//    Map<String, Object> body = new HashMap<>();
//
//    body.put("Header", header);
//    body.put("depositAccountNo", requestDto.getDepositAccountNo());
//    body.put("depositTransactionSummary", requestDto.getDepositTransactionSummary());
//    body.put("transactionBalance", requestDto.getTransactionBalance());
//    body.put("withdrawalAccountNo", accountNo);
//    body.put("withdrawalTransactionSummary", requestDto.getWithdrawalTransactionSummary());
//
//    ResponseEntity<Map<String, Object>> response = webClient.post().uri(API_URL)
//        .contentType(MediaType.APPLICATION_JSON).bodyValue(body).retrieve()
//        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
//        }).block();
//
//    List<Object> recObject = (List<Object>) response.getBody().get("REC");
//
//    List<TransferHistoryResponseDto> responseDto = recObject.stream()
//        .map(value -> modelMapper.map(value, TransferHistoryResponseDto.class))
//        .collect(Collectors.toList());
//
//    withDrawalAccount.setBalance(
//        withDrawalAccount.getBalance() - requestDto.getTransactionBalance());
//    depositAccount.setBalance(depositAccount.getBalance() + requestDto.getTransactionBalance());
//
//    generalAccountRepository.save(withDrawalAccount);
//    generalAccountRepository.save(depositAccount);
//
//    //출금 계좌 알림
//    TransactionNotificationDto withDrawalNotification = TransactionNotificationDto.builder()
//        .userId(userId).accountId(withDrawalAccount.getId())
//        .accountNo(withDrawalAccount.getAccountNo()).amount(requestDto.getTransactionBalance())
//        .balance(withDrawalAccount.getBalance()).message("출금 발생").build();
//    notificationService.notifyTransactionMessage(withDrawalNotification);
//
//    //입금 계좌 알림
//    TransactionNotificationDto depositNotification = TransactionNotificationDto.builder()
//        .userId(userId).accountId(depositAccount.getId()).accountNo(depositAccount.getAccountNo())
//        .amount(requestDto.getTransactionBalance()).balance(depositAccount.getBalance())
//        .message("입금 발생").build();
//    notificationService.notifyTransactionMessage(depositNotification);
//    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    return null;
  }
//
//  public ResponseEntity<List<TransactionHistoryDto>> getHistoryByAccountNo(String accountNo,
//      TransactionHistoryRequestDto requestDto) {
//
//    Long userId = SecurityUtil.getCurrentUserId();
//    User user = userRepository.findByUserId(userId)
//        .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));
//
//    String API_NAME = "inquireTransactionHistoryList";
//    String API_URL = BASE_URL + "/" + API_NAME;
//
//    Header header = Header.builder().apiName(API_NAME).apiServiceCode(API_NAME)
//        .apiKey(apiKeys.get("API_KEY")).userKey(user.getUserKey()).build();
//
//    Map<String, Object> body = new HashMap<>();
//
//    body.put("Header", header);
//    body.put("accountNo", accountNo);
//    body.put("startDate", requestDto.getStartDate());
//    body.put("endDate", requestDto.getEndDate());
//    body.put("transactionType", requestDto.getTransactionType());
//    body.put("orderByType", requestDto.getOrderByType());
//
//    ResponseEntity<Map<String, Object>> response = webClient.post().uri(API_URL)
//        .contentType(MediaType.APPLICATION_JSON).bodyValue(body).retrieve()
//        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
//        }).block();
//
//    Map<String, Object> recObject = (Map<String, Object>) response.getBody().get("REC");
//    List<Object> recList = (List<Object>) recObject.get("list");
//
//    List<TransactionHistoryDto> responseDto = recList.stream()
//        .map(value -> modelMapper.map(value, TransactionHistoryDto.class))
//        .collect(Collectors.toList());
//
//    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//  }
//
//  /**
//   * 외화 계좌에 입금하는 메서드
//   */
//  public DepositResponseDto postForeignDeposit(String accountNo,
//      ForeignTransactionRequestDto requestDto) {
//
//    Long userId = requestDto.getUserId();
//
//    User user = userRepository.findByUserId(userId)
//        .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));
//
//    ForeignAccount foreignAccount = foreignAccountRepository.findByAccountNo(accountNo).orElseThrow(
//        () -> new IllegalArgumentException("The AccountNo Does Not Exist" + accountNo));
//
//    String API_NAME = "updateForeignCurrencyDemandDepositAccountDeposit";
//    String API_URL = BASE_URL + "/foreignCurrency/" + API_NAME;
//
//    Header header = Header.builder().apiName(API_NAME).apiServiceCode(API_NAME)
//        .apiKey(apiKeys.get("API_KEY")).userKey(user.getUserKey()).build();
//
//    Map<String, Object> body = new HashMap<>();
//
//    body.put("Header", header);
//    body.put("accountNo", accountNo);
//    body.put("transactionBalance", requestDto.getTransactionBalance());
//    body.put("transactionSummary", requestDto.getTransactionSummary());
//
//    ResponseEntity<Map<String, Object>> response = webClient.post().uri(API_URL)
//        .contentType(MediaType.APPLICATION_JSON).bodyValue(body).retrieve()
//        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
//        }).block();
//
//    Object recObject = response.getBody().get("REC");
//
//    DepositResponseDto depositResponseDto = modelMapper.map(recObject, DepositResponseDto.class);
//
//    Double currentBalance = foreignAccount.getBalance();
//
//    foreignAccount.setBalance(currentBalance + requestDto.getTransactionBalance());
//
//    foreignAccountRepository.save(foreignAccount);
//
//    return depositResponseDto;
//  }
//
//  /**
//   * 외화 계좌에서 출금하는 메서드
//   */
//  public DepositResponseDto postForeignWithdrawal(boolean is_settlement, String accountNo,
//      ForeignTransactionRequestDto requestDto) {
//
//    Long userId = requestDto.getUserId();
//
//    User user = userRepository.findByUserId(userId)
//        .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));
//
//    ForeignAccount foreignAccount = foreignAccountRepository.findByAccountNo(accountNo).orElseThrow(
//        () -> new IllegalArgumentException("The AccountNo Does Not Exist" + accountNo));
//
//    String API_NAME = "updateForeignCurrencyDemandDepositAccountWithdrawal";
//    String API_URL = BASE_URL + "/foreignCurrency/" + API_NAME;
//
//    Header header = Header.builder().apiName(API_NAME).apiServiceCode(API_NAME)
//        .apiKey(apiKeys.get("API_KEY")).userKey(user.getUserKey()).build();
//
//    Map<String, Object> body = new HashMap<>();
//
//    body.put("Header", header);
//    body.put("accountNo", accountNo);
//    body.put("transactionBalance", requestDto.getTransactionBalance());
//    body.put("transactionSummary", requestDto.getTransactionSummary());
//
//    ResponseEntity<Map<String, Object>> response = webClient.post().uri(API_URL)
//        .contentType(MediaType.APPLICATION_JSON).bodyValue(body).retrieve()
//        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
//        }).block();
//
//    Object recObject = response.getBody().get("REC");
//
//    DepositResponseDto responseDto = modelMapper.map(recObject, DepositResponseDto.class);
//
//    Double currentBalance = foreignAccount.getBalance();
//    foreignAccount.setBalance(currentBalance - requestDto.getTransactionBalance());
//
//    if (!is_settlement) {
//      cashHistoryService.getCashFromAccount(foreignAccount, requestDto.getTransactionBalance());
//    }
//
//    foreignAccountRepository.save(foreignAccount);
//
//    return responseDto;
//  }
//
//  /**
//   * 외화 통장 거래 내역 조회
//   */
//  public List<TransactionHistoryDto> getForeignHistoryByAccountNo(String accountNo,
//      TransactionHistoryRequestDto requestDto) {
//
//    Long userId = SecurityUtil.getCurrentUserId();
//    User user = userRepository.findByUserId(userId)
//        .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));
//
//    String API_NAME = "inquireForeignCurrencyTransactionHistoryList";
//    String API_URL = BASE_URL + "/foreignCurrency/" + API_NAME;
//
//    Header header = Header.builder().apiName(API_NAME).apiServiceCode(API_NAME)
//        .apiKey(apiKeys.get("API_KEY")).userKey(user.getUserKey()).build();
//
//    Map<String, Object> body = new HashMap<>();
//
//    body.put("Header", header);
//    body.put("accountNo", accountNo);
//    body.put("startDate", requestDto.getStartDate());
//    body.put("endDate", requestDto.getEndDate());
//    body.put("transactionType", requestDto.getTransactionType());
//    body.put("orderByType", requestDto.getOrderByType());
//
//    ResponseEntity<Map<String, Object>> response = webClient.post().uri(API_URL)
//        .contentType(MediaType.APPLICATION_JSON).bodyValue(body).retrieve()
//        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
//        }).block();
//
//    Map<String, Object> recObject = (Map<String, Object>) response.getBody().get("REC");
//    List<Object> recList = (List<Object>) recObject.get("list");
//
//    List<TransactionHistoryDto> responseDto = recList.stream()
//        .map(value -> modelMapper.map(value, TransactionHistoryDto.class))
//        .collect(Collectors.toList());
//
//    return responseDto;
//  }
}
