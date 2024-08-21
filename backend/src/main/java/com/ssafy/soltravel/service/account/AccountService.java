package com.ssafy.soltravel.service.account;

import com.ssafy.soltravel.common.Header;
import com.ssafy.soltravel.domain.GeneralAccount;
import com.ssafy.soltravel.dto.account.AccountDto;
import com.ssafy.soltravel.dto.account.response.CreateAccountResponseDto;
import com.ssafy.soltravel.dto.account.response.DeleteAccountResponseDto;
import com.ssafy.soltravel.repository.GeneralAccountRepository;
import com.ssafy.soltravel.util.LogUtil;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final Map<String, String> apiKeys;
    private final WebClient webClient;
    private final ModelMapper modelMapper;

    private final GeneralAccountRepository generalAccountRepository;

    private final String BASE_URL = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit";

    public ResponseEntity<CreateAccountResponseDto> createAccount(Long userId) {

        String API_NAME = "createDemandDepositAccount";
        String API_URL = BASE_URL + "/" + API_NAME;

        // 추후에 userId 받아서 userKey 수정
        // 현재는 유저 구현 안되서 임시로 처리함
        Header header = Header.builder()
            .apiName(API_NAME)
            .apiServiceCode(API_NAME)
            .apiKey(apiKeys.get("API_KEY"))
            .userKey(apiKeys.get("USER_KEY"))
            .build();

        Map<String, Object> body = new HashMap<>();
        body.put("Header", header);
        body.put("accountTypeUniqueNo", apiKeys.get("ACCOUNT_UNIQUE_NO"));

        try {
            // 일반 계좌 DB 저장 로직 유저 완성 시 추가
            ResponseEntity<Map<String, Object>> response = webClient.post()
                .uri(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();

            // REC 부분을 Object 타입으로 받기
            Object recObject = response.getBody().get("REC");

            ModelMapper modelMapper = new ModelMapper();

            CreateAccountResponseDto responseDto = modelMapper.map(recObject, CreateAccountResponseDto.class);

            // REC 데이터를 GeneralAccount 엔티티로 변환
            GeneralAccount generalAccount = modelMapper.map(responseDto, GeneralAccount.class);
            generalAccount.setBalance(0L);

            generalAccountRepository.save(generalAccount);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (WebClientResponseException e) {
            throw e;
        }
    }

    public ResponseEntity<List<AccountDto>> getAllByUserId(Long userId) {
        String API_NAME = "inquireDemandDepositAccountList";
        String API_URL = BASE_URL + "/" + API_NAME;

        Header header = Header.builder()
            .apiName(API_NAME)
            .apiServiceCode(API_NAME)
            .apiKey(apiKeys.get("API_KEY"))
            .userKey(apiKeys.get("USER_KEY"))
            .build();

        Map<String, Object> body = new HashMap<>();
        body.put("Header", header);

        try {
            ResponseEntity<Map<String, Object>> response = webClient.post()
                .uri(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();

            // REC 부분을 Object 타입으로 받기
            Object recObject = response.getBody().get("REC");

            ModelMapper modelMapper = new ModelMapper();

            LogUtil.info("recObject", recObject);

//            // REC 데이터를 GeneralAccount 엔티티로 변환
//            List<AccountDto> responseDto = modelMapper.map(recObject, DeleteAccountResponseDto.class);
//
//            generalAccountRepository.deleteByAccountNo(accountNo);
//
//            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
            return null;
        } catch (Exception e) {
            throw e;
        }
    }


    public ResponseEntity<DeleteAccountResponseDto> deleteAccount(String accountNo) {

        String API_NAME = "deleteDemandDepositAccount";
        String API_URL = BASE_URL + "/" + API_NAME;

        Header header = Header.builder()
            .apiName(API_NAME)
            .apiServiceCode(API_NAME)
            .apiKey(apiKeys.get("API_KEY"))
            .userKey(apiKeys.get("USER_KEY"))
            .build();

        Map<String, Object> body = new HashMap<>();
        body.put("Header", header);
        body.put("accountNo", String.valueOf(accountNo));

        try {
            ResponseEntity<Map<String, Object>> response = webClient.post()
                .uri(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();

            // REC 부분을 Object 타입으로 받기
            Object recObject = response.getBody().get("REC");

            ModelMapper modelMapper = new ModelMapper();

            // REC 데이터를 GeneralAccount 엔티티로 변환
            DeleteAccountResponseDto responseDto = modelMapper.map(recObject, DeleteAccountResponseDto.class);

            generalAccountRepository.deleteByAccountNo(accountNo);

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (WebClientResponseException e) {
            throw e;
        }
    }


}
