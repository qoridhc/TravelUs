package com.ssafy.soltravel.service.transaction;

import com.ssafy.soltravel.common.Header;
import com.ssafy.soltravel.dto.transaction.TransactionHistoryDto;
import com.ssafy.soltravel.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.dto.transaction.response.DepositResponseDto;
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

    private final String BASE_URL = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit";

    public ResponseEntity<DepositResponseDto> postAccountDeposit(String accountNo, TransactionRequestDto requestDto) {

        String API_NAME = "updateDemandDepositAccountDeposit";
        String API_URL = BASE_URL + "/" + API_NAME;

        Header header = Header.builder()
            .apiName(API_NAME)
            .apiServiceCode(API_NAME)
            .apiKey(apiKeys.get("API_KEY"))
            .userKey(apiKeys.get("USER_KEY"))
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

        String API_NAME = "updateDemandDepositAccountWithdrawal";
        String API_URL = BASE_URL + "/" + API_NAME;

        Header header = Header.builder()
            .apiName(API_NAME)
            .apiServiceCode(API_NAME)
            .apiKey(apiKeys.get("API_KEY"))
            .userKey(apiKeys.get("USER_KEY"))
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

}
