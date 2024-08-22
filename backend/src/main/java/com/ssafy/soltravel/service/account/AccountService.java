package com.ssafy.soltravel.service.account;

import com.ssafy.soltravel.common.Header;
import com.ssafy.soltravel.domain.ForeignAccount;
import com.ssafy.soltravel.domain.GeneralAccount;
import com.ssafy.soltravel.dto.account.AccountDto;
import com.ssafy.soltravel.dto.account.CreateAccountDto;
import com.ssafy.soltravel.dto.account.request.CreateAccountRequestDto;
import com.ssafy.soltravel.dto.account.response.CreateAccountResponseDto;
import com.ssafy.soltravel.dto.account.response.DeleteAccountResponseDto;
import com.ssafy.soltravel.mapper.AccountMapper;
import com.ssafy.soltravel.repository.ForeignAccountRepository;
import com.ssafy.soltravel.repository.GeneralAccountRepository;
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
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final Map<String, String> apiKeys;
    private final WebClient webClient;
    private final ModelMapper modelMapper;

    private final GeneralAccountRepository generalAccountRepository;
    private final ForeignAccountRepository foreignAccountRepository;

    private final String BASE_URL = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit";

    public CreateAccountResponseDto createGeneralAccount(Long userId, CreateAccountRequestDto requestDto) {

        String API_NAME = "createDemandDepositAccount";

        Map<String, Object> body = new HashMap<>();

        String API_URL = BASE_URL + "/" + API_NAME;

        // 추후에 userId 받아서 userKey 수정
        // 현재는 유저 구현 안되서 임시로 처리함
        Header header = Header.builder()
            .apiName(API_NAME)
            .apiServiceCode(API_NAME)
            .apiKey(apiKeys.get("API_KEY"))
            .userKey(apiKeys.get("USER_KEY"))
            .build();

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

            // generalAccount 생성 & 저장
            GeneralAccount generalAccount = modelMapper.map(recObject, GeneralAccount.class);
            generalAccount.setBalance(0L);

            generalAccountRepository.save(generalAccount);

            // foreingAccount 생성 & 저장
            ForeignAccount foreignAccount = createForeignAccount(userId, requestDto, generalAccount.getId());

            // dto 변환
            CreateAccountDto generalAccountDto = AccountMapper.toCreateAccountDto(generalAccount);
            CreateAccountDto foreignAccountDto = AccountMapper.toCreateAccountDto(foreignAccount);

            // 응답 dto 생성
            CreateAccountResponseDto responseDto = new CreateAccountResponseDto(generalAccountDto, foreignAccountDto);

            return responseDto;
        } catch (WebClientResponseException e) {
            throw e;
        }
    }

    public ForeignAccount createForeignAccount(Long userId, CreateAccountRequestDto requestDto, Long accountId) {

        String API_NAME = "createForeignCurrencyDemandDepositAccount";

        String API_URL = BASE_URL + "/foreignCurrency/" + API_NAME;

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
        body.put("accountTypeUniqueNo", apiKeys.get("FOREIGN_UNIQUE_ACCOUNT_NO"));
        body.put("currency", requestDto.getCurrencyCode());

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
            Map<String,String> recObject = (Map<String, String>) response.getBody().get("REC");

            GeneralAccount generalAccount = generalAccountRepository.findById(accountId).orElseThrow(() ->  new RuntimeException());
            ForeignAccount foreignAccount = AccountMapper.toForeignAccountEntitiy(recObject, generalAccount, requestDto);

            foreignAccountRepository.save(foreignAccount);

            return foreignAccount;
        } catch (WebClientResponseException e) {
            throw e;
        }
    }

    public ResponseEntity<AccountDto> getByAccountNo(String accountNo) {
        String API_NAME = "inquireDemandDepositAccount";

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
        body.put("accountNo",accountNo);

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
            Map<String,String> recObject = (Map<String, String>) response.getBody().get("REC");

            AccountDto accountDto = modelMapper.map(recObject, AccountDto.class);

            return ResponseEntity.status(HttpStatus.OK).body(accountDto);
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
            List<Object> recObject = (List<Object>) response.getBody().get("REC");

            List<AccountDto> responseDto = recObject.stream()
                .map(value -> modelMapper.map(value, AccountDto.class))
                .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
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
