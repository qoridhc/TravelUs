package com.ssafy.soltravel.v2.service.transaction;

import com.ssafy.soltravel.v2.common.Header;
import com.ssafy.soltravel.v2.domain.Enum.TransactionType;
import com.ssafy.soltravel.v2.dto.transaction.request.MoneyBoxTransferRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransactionHistoryRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransferRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.response.TransactionResponseDto;
import com.ssafy.soltravel.v2.dto.transaction.response.TransferHistoryResponseDto;
import com.ssafy.soltravel.v2.repository.UserRepository;
import com.ssafy.soltravel.v2.util.LogUtil;
import com.ssafy.soltravel.v2.util.WebClientUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

  private final String BASE_URL = "http://localhost:8080/api/v1/bank/transaction";
  private final Map<String, String> apiKeys;
  private final WebClientUtil webClientUtil;
  private final ModelMapper modelMapper;
  private final UserRepository userRepository;

  /**
   * 입금
   */
  public ResponseEntity<TransactionResponseDto> postAccountDeposit(
      TransactionRequestDto requestDto) {

    return processTransaction("deposit", requestDto);
  }

  /**
   * 출금
   */
  public ResponseEntity<TransactionResponseDto> postAccountWithdrawal(
      TransactionRequestDto requestDto) {

    return processTransaction("withdrawal", requestDto);
  }

  /**
   * 일반 이체
   */
  public ResponseEntity<List<TransferHistoryResponseDto>> postGeneralTransfer(
      TransferRequestDto requestDto) {

    return processTransfer("general", requestDto);
  }

  /**
   * 머니 박스 이체
   */
  public ResponseEntity<List<TransferHistoryResponseDto>> postMoneyBoxTransfer(
      MoneyBoxTransferRequestDto requestDto) {

    return processMoneyBoxTransfer("moneybox", requestDto);
  }

  /**
   * 입출금 공통 요청 처리 메서드
   */
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

    TransactionResponseDto transactionResponseDto = modelMapper.map(recObject,
        TransactionResponseDto.class);
    return ResponseEntity.status(HttpStatus.OK).body(transactionResponseDto);
  }


  /**
   * 이체 요청 처리 메서드
   */
  public ResponseEntity<List<TransferHistoryResponseDto>> processTransfer(String apiName,
      TransferRequestDto requestDto) {

//    Long userId = SecurityUtil.getCurrentUserId();
//    User user = userRepository.findByUserId(userId)
//        .orElseThrow(() -> new UserNotFoundException(userId));

    String API_URL = BASE_URL + "/transfer/" + apiName;

    Header header = Header.builder()
//        .apiKey(apiKeys.get("API_KEY"))
//        .userKey(user.getUserKey()).build();
        .apiKey("V4BYi-78qMmIyXoJOcnCXRE0TXIyWgjBlZcAYe4JIljMu6of_GJ8kbUBWlfqW0WN")
        .userKey("779e4e7e-ccd7-420c-92b5-8770cfd9199e").build();

    Map<String, Object> body = new HashMap<>();
    body.put("Header", header);
    body.put("transferType", requestDto.getTransferType());
    body.put("withdrawalAccountId", requestDto.getWithdrawalAccountId());
    body.put("depositAccountId", requestDto.getDepositAccountId());
    body.put("transactionBalance", requestDto.getTransactionBalance());
    body.put("withdrawalTransactionSummary", requestDto.getWithdrawalTransactionSummary());
    body.put("depositTransactionSummary", requestDto.getDepositTransactionSummary());

    ResponseEntity<Map<String, Object>> response = webClientUtil.request(API_URL, body, Map.class);

    List<Object> recObject = (List<Object>) response.getBody().get("REC");

    List<TransferHistoryResponseDto> responseDto = recObject.stream()
        .map(value -> modelMapper.map(value, TransferHistoryResponseDto.class))
        .collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

  /**
   * 머니박스 이체 요청 처리 메서드
   */
  public ResponseEntity<List<TransferHistoryResponseDto>> processMoneyBoxTransfer(String apiName,
      MoneyBoxTransferRequestDto requestDto) {

//    Long userId = SecurityUtil.getCurrentUserId();
//    User user = userRepository.findByUserId(userId)
//        .orElseThrow(() -> new UserNotFoundException(userId));

    String API_URL = BASE_URL + "/transfer/" + apiName;

    Header header = Header.builder()
//        .apiKey(apiKeys.get("API_KEY"))
//        .userKey(user.getUserKey()).build();
        .apiKey("V4BYi-78qMmIyXoJOcnCXRE0TXIyWgjBlZcAYe4JIljMu6of_GJ8kbUBWlfqW0WN")
        .userKey("779e4e7e-ccd7-420c-92b5-8770cfd9199e").build();

    Map<String, Object> body = new HashMap<>();
    body.put("Header", header);
    body.put("transferType", requestDto.getTransferType());
    body.put("accountId", requestDto.getAccountId());
    body.put("sourceCurrencyId", requestDto.getSourceCurrencyId());
    body.put("targetCurrencyId", requestDto.getTargetCurrencyId());
    body.put("transactionBalance", requestDto.getTransactionBalance());

    ResponseEntity<Map<String, Object>> response = webClientUtil.request(API_URL, body, Map.class);

    List<Object> recObject = (List<Object>) response.getBody().get("REC");

    List<TransferHistoryResponseDto> responseDto = recObject.stream()
        .map(value -> modelMapper.map(value, TransferHistoryResponseDto.class))
        .collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

  /**
   * 거래 내역 조회
   */
  public ResponseEntity<List<TransferHistoryResponseDto>> getHistory(
      TransactionHistoryRequestDto requestDto) {

    //    Long userId = SecurityUtil.getCurrentUserId();
//    User user = userRepository.findByUserId(userId)
//        .orElseThrow(() -> new UserNotFoundException(userId));

    String API_URL = BASE_URL + "/history";

    Header header = Header.builder()
//        .apiKey(apiKeys.get("API_KEY"))
//        .userKey(user.getUserKey()).build();
        .apiKey("V4BYi-78qMmIyXoJOcnCXRE0TXIyWgjBlZcAYe4JIljMu6of_GJ8kbUBWlfqW0WN")
        .userKey("779e4e7e-ccd7-420c-92b5-8770cfd9199e").build();

    LogUtil.info("requestDto: ", requestDto);
    Map<String, Object> body = new HashMap<>();
    body.put("Header", header);
    TransactionType transactionType = requestDto.getTransactionType();
    if (transactionType != null) {
      body.put("transactionType", transactionType);
    }
    body.put("moneyBoxId", requestDto.getMoneyBoxId());
    body.put("startDate", requestDto.getStartDate());
    body.put("endDate", requestDto.getEndDate());
    body.put("orderByType", requestDto.getOrderByType());

    ResponseEntity<Map<String, Object>> response = webClientUtil.request(API_URL, body, Map.class);

    List<Object> recObject = (List<Object>) response.getBody().get("REC");

    List<TransferHistoryResponseDto> responseDto = recObject.stream()
        .map(value -> modelMapper.map(value, TransferHistoryResponseDto.class))
        .collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }
}
