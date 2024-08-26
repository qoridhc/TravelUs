package com.ssafy.soltravel.service.transaction;

import com.ssafy.soltravel.common.Header;
import com.ssafy.soltravel.domain.User;
import com.ssafy.soltravel.dto.transaction.TransactionHistoryDto;
import com.ssafy.soltravel.dto.transaction.request.ForeignTransactionRequestDto;
import com.ssafy.soltravel.dto.transaction.request.TransactionHistoryRequestDto;
import com.ssafy.soltravel.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.dto.transaction.request.TransferRequestDto;
import com.ssafy.soltravel.dto.transaction.response.DepositResponseDto;
import com.ssafy.soltravel.dto.transaction.response.TransferHistoryResponseDto;
import com.ssafy.soltravel.repository.UserRepository;
import com.ssafy.soltravel.util.LogUtil;
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

    private final String BASE_URL = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit";

    public ResponseEntity<DepositResponseDto> postAccountDeposit(String accountNo, TransactionRequestDto requestDto) {

        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));

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

        return ResponseEntity.status(HttpStatus.OK).body(depositResponseDto);
    }

    public ResponseEntity<DepositResponseDto> postAccountWithdrawal(String accountNo, TransactionRequestDto requestDto) {

        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));

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

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    public ResponseEntity<List<TransferHistoryResponseDto>> postAccountTransfer(String accountNo, TransferRequestDto requestDto) {

        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));

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

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    public ResponseEntity<List<TransactionHistoryDto>> getHistoryByAccountNo(String accountNo, TransactionHistoryRequestDto requestDto) {

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
    public DepositResponseDto postForeignDeposit(String accountNo, ForeignTransactionRequestDto requestDto) {

        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));

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

        return depositResponseDto;

    }
    /**
     * 외화 통장 거래 내역 조회
     */
    public List<TransactionHistoryDto> getForeignHistoryByAccountNo(String accountNo, TransactionHistoryRequestDto requestDto) {

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
