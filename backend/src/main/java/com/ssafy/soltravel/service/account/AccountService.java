package com.ssafy.soltravel.service.account;

import com.ssafy.soltravel.common.Header;
import com.ssafy.soltravel.domain.Enum.AccountType;
import com.ssafy.soltravel.domain.ForeignAccount;
import com.ssafy.soltravel.domain.GeneralAccount;
import com.ssafy.soltravel.domain.Participant;
import com.ssafy.soltravel.domain.User;
import com.ssafy.soltravel.dto.ResponseDto;
import com.ssafy.soltravel.dto.account.AccountDto;
import com.ssafy.soltravel.dto.account.CreateAccountDto;
import com.ssafy.soltravel.dto.account.request.CreateAccountRequestDto;
import com.ssafy.soltravel.dto.account.response.CreateAccountResponseDto;
import com.ssafy.soltravel.dto.account.response.DeleteAccountResponseDto;
import com.ssafy.soltravel.dto.participants.ParticipantDto;
import com.ssafy.soltravel.dto.participants.request.AddParticipantRequestDto;
import com.ssafy.soltravel.dto.participants.request.ParticipantListResponseDto;
import com.ssafy.soltravel.mapper.AccountMapper;
import com.ssafy.soltravel.mapper.ParticipantMapper;
import com.ssafy.soltravel.repository.ForeignAccountRepository;
import com.ssafy.soltravel.repository.GeneralAccountRepository;
import com.ssafy.soltravel.repository.ParticipantRepository;
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
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final Map<String, String> apiKeys;
    private final WebClient webClient;
    private final ModelMapper modelMapper;

    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final GeneralAccountRepository generalAccountRepository;
    private final ForeignAccountRepository foreignAccountRepository;

    private final String BASE_URL = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit";

    public CreateAccountResponseDto createGeneralAccount(
        Long userId,
        CreateAccountRequestDto requestDto) {

        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));

        String API_NAME = "createDemandDepositAccount";

        Map<String, Object> body = new HashMap<>();

        String API_URL = BASE_URL + "/" + API_NAME;

        Header header = Header.builder()
            .apiName(API_NAME)
            .apiServiceCode(API_NAME)
            .apiKey(apiKeys.get("API_KEY"))
            .userKey(user.getUserKey())
            .build();

        body.put("Header", header);
        body.put("accountTypeUniqueNo", apiKeys.get("ACCOUNT_UNIQUE_NO"));

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

        // generalAccount 생성 & 저장
        GeneralAccount generalAccount = modelMapper.map(recObject, GeneralAccount.class);
        generalAccount.setBalance(0L);
        generalAccount.setUser(user);
        generalAccount.setAccountType(requestDto.getAccountType());

        generalAccountRepository.save(generalAccount);

        CreateAccountResponseDto responseDto = new CreateAccountResponseDto();

        CreateAccountDto generalAccountDto = AccountMapper.toCreateAccountDto(generalAccount);

        responseDto.setGeneralAccount(generalAccountDto);

        // 토임 통장인경우 -> 외화 모임통장 자동 생성 & 그룹장으로 본인 참여자에 추가
        if (requestDto.getAccountType().equals(AccountType.GROUP)) {

            // foreingAccount 생성 & 저장
            ForeignAccount foreignAccount = createForeignAccount(user, requestDto,
                generalAccount.getId());

            Participant participant = Participant.builder()
                .isMaster(true)
                .user(user)
                .generalAccount(generalAccount)
                .build();

            // 참여자로 본인 추가
            participantRepository.save(participant);

            // dto 변환
            CreateAccountDto foreignAccountDto = AccountMapper.toCreateAccountDto(foreignAccount);

            responseDto.setForeignAccount(foreignAccountDto);
        }

        return responseDto;
    }

    public ForeignAccount createForeignAccount(User user, CreateAccountRequestDto requestDto, Long accountId) {

        String API_NAME = "createForeignCurrencyDemandDepositAccount";

        String API_URL = BASE_URL + "/foreignCurrency/" + API_NAME;

        // 추후에 userId 받아서 userKey 수정
        // 현재는 유저 구현 안되서 임시로 처리함
        Header header = Header.builder()
            .apiName(API_NAME)
            .apiServiceCode(API_NAME)
            .apiKey(apiKeys.get("API_KEY"))
            .userKey(user.getUserKey())
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
            Map<String, String> recObject = (Map<String, String>) response.getBody().get("REC");

            GeneralAccount generalAccount = generalAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException());

            ForeignAccount foreignAccount = AccountMapper.toForeignAccountEntitiy(recObject,
                generalAccount,
                requestDto);

            foreignAccountRepository.save(foreignAccount);

            return foreignAccount;
        } catch (WebClientResponseException e) {
            throw e;
        }
    }

    public ResponseEntity<AccountDto> getByAccountNo(String accountNo, boolean isForeign) {

        Long userId = SecurityUtil.getCurrentUserId();

        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));

        String API_NAME = "inquireDemandDepositAccount";
        String API_URL = BASE_URL + "/" + API_NAME;

        if (isForeign) {
            API_NAME = "inquireForeignCurrencyDemandDepositAccount";
            API_URL = BASE_URL + "/foreignCurrency/" + API_NAME;
        }

        // 추후에 userId 받아서 userKey 수정
        // 현재는 유저 구현 안되서 임시로 처리함
        Header header = Header.builder()
            .apiName(API_NAME)
            .apiServiceCode(API_NAME)
            .apiKey(apiKeys.get("API_KEY"))
            .userKey(user.getUserKey())
            .build();

        Map<String, Object> body = new HashMap<>();
        body.put("Header", header);
        body.put("accountNo", accountNo);

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
            Map<String, String> recObject = (Map<String, String>) response.getBody().get("REC");

            AccountDto accountDto = modelMapper.map(recObject, AccountDto.class);

            return ResponseEntity.status(HttpStatus.OK).body(accountDto);
        } catch (WebClientResponseException e) {
            throw e;
        }
    }

    public ResponseEntity<List<AccountDto>> getAllByUserId(Long userId, boolean isForeign) {

        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));

        String API_NAME = "inquireDemandDepositAccountList";
        String API_URL = BASE_URL + "/" + API_NAME;

        if (isForeign) {
            API_NAME = "inquireForeignCurrencyDemandDepositAccountList";
            API_URL = BASE_URL + "/foreignCurrency/" + API_NAME;
        }

        Header header = Header.builder()
            .apiName(API_NAME)
            .apiServiceCode(API_NAME)
            .apiKey(apiKeys.get("API_KEY"))
            .userKey(user.getUserKey())
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

    public ResponseEntity<DeleteAccountResponseDto> deleteAccount(String accountNo, boolean isForeign) {

        Long userId = SecurityUtil.getCurrentUserId();

        LogUtil.info("userId", userId);

        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));

        String API_NAME = "deleteDemandDepositAccount";
        String API_URL = BASE_URL + "/" + API_NAME;

        if (isForeign) {
            API_NAME = "deleteForeignCurrencyDemandDepositAccount";
            API_URL = BASE_URL + "/foreignCurrency/" + API_NAME;
        }

        Header header = Header.builder()
            .apiName(API_NAME)
            .apiServiceCode(API_NAME)
            .apiKey(apiKeys.get("API_KEY"))
            .userKey(user.getUserKey())
            .build();

        Map<String, Object> body = new HashMap<>();
        body.put("Header", header);
        body.put("accountNo", accountNo);

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

            if (isForeign) {
                foreignAccountRepository.deleteByAccountNo(accountNo);
            } else {
                generalAccountRepository.deleteByAccountNo(accountNo);
            }

            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (WebClientResponseException e) {
            throw e;
        }
    }

    public ResponseEntity<ResponseDto> addParticipant(Long accountId,
        AddParticipantRequestDto requestDto) {

        GeneralAccount generalAccount = generalAccountRepository.findById(accountId).orElseThrow(
            () -> new IllegalArgumentException("The generalAccountId does not exist: " + accountId));

        User user = userRepository.findByUserId(requestDto.getParticipantId()).orElseThrow(
            () -> new IllegalArgumentException(
                "The participantId does not exist: " + requestDto.getParticipantId()));

        Participant participant = Participant.builder()
            .isMaster(false)
            .generalAccount(generalAccount)
            .user(user)
            .build();

        participantRepository.save(participant);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto());
    }

    public ResponseEntity<ParticipantListResponseDto> getParticipants(Long accountId) {

        List<Participant> participants = participantRepository.findAllByGeneralAccountId(accountId);

        List<ParticipantDto> participantDtoList = participants.stream()
            .map(ParticipantMapper::toDto)
            .collect(Collectors.toList());

        ParticipantListResponseDto responseDto = ParticipantListResponseDto.builder()
            .accountId(accountId)
            .participants(participantDtoList)
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    public List<Long> findUserIdsByGeneralAccountId(Long accountId) {

        return participantRepository.findUserIdsByGeneralAccountId(accountId);
    }

    public Long getBalanceByAccountId(Long accountId) {
        return generalAccountRepository.findBalanceByAccountId(accountId);
    }

    public ForeignAccount getForeignAccount(long accountId) {

        return foreignAccountRepository.findById(accountId).get();
    }
}
