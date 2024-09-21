package com.ssafy.soltravel.v2.service.account;


import com.ssafy.soltravel.v2.common.BankHeader;
import com.ssafy.soltravel.v2.common.Header;
import com.ssafy.soltravel.v2.domain.Enum.AccountType;
import com.ssafy.soltravel.v2.domain.ForeignAccount;
import com.ssafy.soltravel.v2.domain.GeneralAccount;
import com.ssafy.soltravel.v2.domain.Participant;
import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.dto.ResponseDto;
import com.ssafy.soltravel.v2.dto.account.AccountDto;
import com.ssafy.soltravel.v2.dto.account.request.CreateAccountRequestDto;
import com.ssafy.soltravel.v2.dto.account.request.DeleteAccountRequestDto;
import com.ssafy.soltravel.v2.dto.account.response.CreateAccountResponseDto;
import com.ssafy.soltravel.v2.dto.account.response.DeleteAccountResponseDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateRegisterRequestDto;
import com.ssafy.soltravel.v2.dto.participants.ParticipantDto;
import com.ssafy.soltravel.v2.dto.participants.request.AddParticipantRequestDto;
import com.ssafy.soltravel.v2.dto.participants.request.ParticipantListResponseDto;
import com.ssafy.soltravel.v2.dto.user.EmailValidationDto;
import com.ssafy.soltravel.v2.exception.RefundAccountNotFoundException;
import com.ssafy.soltravel.v2.mapper.AccountMapper;
import com.ssafy.soltravel.v2.mapper.ParticipantMapper;
import com.ssafy.soltravel.v2.repository.ForeignAccountRepository;
import com.ssafy.soltravel.v2.repository.GeneralAccountRepository;
import com.ssafy.soltravel.v2.repository.ParticipantRepository;
import com.ssafy.soltravel.v2.repository.UserRepository;
import com.ssafy.soltravel.v2.util.LogUtil;
import com.ssafy.soltravel.v2.util.SecurityUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.PropertySource;
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
  private final AccountMapper accountMapper;

  private final UserRepository userRepository;
  private final ParticipantRepository participantRepository;
  private final GeneralAccountRepository generalAccountRepository;
  private final ForeignAccountRepository foreignAccountRepository;
  private final AccountExchangeService accountExchangeService;

  private final String BASE_URL = "http://localhost:8085/api/v1/bank/accounts/";

  public Map<String, Object> createGeneralAccount(
      Long userId,
      CreateAccountRequestDto requestDto
  ) {
//
//    User user = userRepository.findByUserId(userId)
//        .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));
//
//    String API_NAME = "createDemandDepositAccount";

    Map<String, Object> body = new HashMap<>();

    String API_URL = BASE_URL + "postAccount";

    BankHeader header = BankHeader.createHeader(requestDto.getUserKey());

    LogUtil.info("header", header);

    body.put("Header", header);
    body.put("accountType", requestDto.getAccountType());
    body.put("accountPassword", requestDto.getAcocuntPassword());
    body.put("bankId", requestDto.getBankId());

    // 일반 계좌 DB 저장 로직 유저 완성 시 추가
    ResponseEntity<Map<String, Object>> response = webClient.post()
        .uri(API_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .retrieve()
        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
        })
        .block();

//    // REC 부분을 Object 타입으로 받기
    Map<String, Object> recObject = response.getBody();


    LogUtil.info("recObject", recObject);
//    Map<String, String> recObject = response.getBody();

//    GeneralAccount generalAccount = GeneralAccount.fromRecObject(recObject);
//
//    generalAccountRepository.save(generalAccount);


    return recObject;
  }

//  public ForeignAccount createForeignAccount(User user, CreateAccountRequestDto requestDto,
//      Long accountId) {
//
//    String API_NAME = "createForeignCurrencyDemandDepositAccount";
//
//    String API_URL = BASE_URL + "/foreignCurrency/" + API_NAME;
//
//    // 추후에 userId 받아서 userKey 수정
//    // 현재는 유저 구현 안되서 임시로 처리함
//    Header header = Header.builder()
//        .apiName(API_NAME)
//        .apiServiceCode(API_NAME)
//        .apiKey(apiKeys.get("API_KEY"))
//        .userKey(user.getUserKey())
//        .build();
//
//    Map<String, Object> body = new HashMap<>();
//    body.put("Header", header);
//    body.put("accountTypeUniqueNo", apiKeys.get("FOREIGN_UNIQUE_ACCOUNT_NO"));
//    body.put("currency", requestDto.getCurrencyCode());
//
//    try {
//      // 일반 계좌 DB 저장 로직 유저 완성 시 추가
//      ResponseEntity<Map<String, Object>> response = webClient.post()
//          .uri(API_URL)
//          .contentType(MediaType.APPLICATION_JSON)
//          .bodyValue(body)
//          .retrieve()
//          .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
//          })
//          .block();
//
//      // REC 부분을 Object 타입으로 받기
//      Map<String, String> recObject = (Map<String, String>) response.getBody().get("REC");
//
//      GeneralAccount generalAccount = generalAccountRepository.findById(accountId)
//          .orElseThrow(() -> new RuntimeException());
//
//      ForeignAccount foreignAccount = accountMapper.toForeignAccountEntitiy(
//          recObject,
//          generalAccount,
//          requestDto
//      );
//
//      ForeignAccount savedAccount = foreignAccountRepository.save(foreignAccount);
//
//      return savedAccount;
//    } catch (WebClientResponseException e) {
//      throw e;
//    }
//  }
//
//  // 계좌 단건 조회 (AccountNo로) - 상세 정보 X
//  public ResponseEntity<AccountDto> getByAccountNo(String accountNo, boolean isForeign) {
//
//    Long userId = SecurityUtil.getCurrentUserId();
//
//    User user = userRepository.findByUserId(userId)
//        .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));
//
//    AccountDto accountDto = null;
//
//    if (isForeign) {
//      ForeignAccount foreignAccount = foreignAccountRepository.findByAccountNo(accountNo)
//          .orElseThrow(
//              () -> new IllegalArgumentException("The accountNo does not exist: " + accountNo));
//
//      accountDto = accountMapper.toCreateAccountDto(foreignAccount);
//    } else {
//      GeneralAccount generalAccount = generalAccountRepository.findByAccountNo(accountNo)
//          .orElseThrow(
//              () -> new IllegalArgumentException("The accountNo does not exist: " + accountNo));
//
//      accountDto = accountMapper.toCreateAccountDto(generalAccount);
//
//    }
//    return ResponseEntity.status(HttpStatus.OK).body(accountDto);
//  }
//
//
//  // 외화 계좌 단건 조회 (AccountID로)- 상세 정보 X
//  public ResponseEntity<AccountDto> getForeignByAccountId(Long accountId) {
//
//    Long userId = SecurityUtil.getCurrentUserId();
//
//    User user = userRepository.findByUserId(userId)
//        .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));
//
//    AccountDto accountDto = null;
//
//      ForeignAccount foreignAccount = foreignAccountRepository.findById(accountId)
//          .orElseThrow(
//              () -> new IllegalArgumentException("The accountId does not exist: " + accountId));
//
//      accountDto = accountMapper.toCreateAccountDto(foreignAccount);
//
//    return ResponseEntity.status(HttpStatus.OK).body(accountDto);
//  }
//
//  /*
//   * 유저 계좌 전체 조회(기본정보)
//   */
//  public ResponseEntity<List<AccountDto>> getAllByUserId(Long userId, boolean isForeign) {
//
//    User user = userRepository.findByUserId(userId)
//        .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));
//
//    List<AccountDto> accountDtos = null;
//
//    if (isForeign) {
//      List<ForeignAccount> foreignAccounts = foreignAccountRepository.findAllByUserId(userId);
//
//      accountDtos = foreignAccounts.stream().map(accountMapper::toCreateAccountDto)
//          .collect(Collectors.toList());
//
//    } else {
//      List<GeneralAccount> generalAccounts = generalAccountRepository.findAllByUser_userId(userId);
//
//      accountDtos = generalAccounts.stream().map(accountMapper::toCreateAccountDto)
//          .collect(Collectors.toList());
//    }
//
//    return ResponseEntity.status(HttpStatus.OK).body(accountDtos);
//  }

  // 계좌 디테일 조회
//    public ResponseEntity<AccountDto> getDetailByAccount(String accountNo, boolean isForeign) {
//
//        Long userId = SecurityUtil.getCurrentUserId();
//
//        User user = userRepository.findByUserId(userId)
//            .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));
//
//        String API_NAME = "inquireDemandDepositAccount";
//        String API_URL = BASE_URL + "/" + API_NAME;
//
//        if (isForeign) {
//            API_NAME = "inquireForeignCurrencyDemandDepositAccount";
//            API_URL = BASE_URL + "/foreignCurrency/" + API_NAME;
//        }
//
//        // 추후에 userId 받아서 userKey 수정
//        // 현재는 유저 구현 안되서 임시로 처리함
//        Header header = Header.builder()
//            .apiName(API_NAME)
//            .apiServiceCode(API_NAME)
//            .apiKey(apiKeys.get("API_KEY"))
//            .userKey(user.getUserKey())
//            .build();
//
//        Map<String, Object> body = new HashMap<>();
//        body.put("Header", header);
//        body.put("accountNo", accountNo);
//
//        try {
//            // 일반 계좌 DB 저장 로직 유저 완성 시 추가
//            ResponseEntity<Map<String, Object>> response = webClient.post()
//                .uri(API_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(body)
//                .retrieve()
//                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
//                })
//                .block();
//
//            // REC 부분을 Object 타입으로 받기
//            Map<String, String> recObject = (Map<String, String>) response.getBody().get("REC");
//
//            Long accountId = generalAccountRepository.findAccountIdsByAccountNo(accountNo);
//
//            AccountDto accountDto = modelMapper.map(recObject, AccountDto.class);
//            accountDto.setId(accountId);
//
//            return ResponseEntity.status(HttpStatus.OK).body(accountDto);
//        } catch (WebClientResponseException e) {
//            throw e;
//        }
//    }
//
//
//  public ResponseEntity<DeleteAccountResponseDto> deleteAccount(
//      String accountNo,
//      boolean isForeign,
//      DeleteAccountRequestDto dto
//  ) {
//    Long userId = SecurityUtil.getCurrentUserId();
//    User user = userRepository.findByUserId(userId)
//        .orElseThrow(() -> new IllegalArgumentException("The userId does not exist: " + userId));
//    String API_NAME = "deleteDemandDepositAccount";
//    String API_URL = BASE_URL + "/" + API_NAME;
//    Double refundAmount = 0.0;
//    if (isForeign) {
//      API_NAME = "deleteForeignCurrencyDemandDepositAccount";
//      API_URL = BASE_URL + "/foreignCurrency/" + API_NAME;
//      ForeignAccount foreignAccount = foreignAccountRepository.findByAccountNo(accountNo)
//          .orElseThrow(() -> new IllegalArgumentException(
//              "The foreignAccountNo does not exist: " + accountNo)
//          );
//      refundAmount = foreignAccount.getBalance();
//      if (refundAmount > 0) {
//        if (dto == null) {
//          throw new RefundAccountNotFoundException();
//        } else if (dto.getRefundAccountNo() == null || dto.getRefundAccountNo().isBlank()) {
//          throw new RefundAccountNotFoundException();
//        }
//      }
//    } else {
//      GeneralAccount generalAccount = generalAccountRepository.findByAccountNo(accountNo)
//          .orElseThrow(() -> new IllegalArgumentException(
//              "The generalAccountNo does not exist: " + accountNo));
//
//      LogUtil.info("refundAmount: ", refundAmount);
//      LogUtil.info("refundAmount > 0: ", refundAmount > 0);
//
//      // 잔액이 남아 있으면
//      if (refundAmount > 0) {
//        if (dto == null) {
//          throw new RefundAccountNotFoundException();
//        } else if (dto.getRefundAccountNo() == null || dto.getRefundAccountNo().isBlank()) {
//          throw new RefundAccountNotFoundException();
//        }
//      }
//    }
//
//    Header header = Header.builder()
//        .apiName(API_NAME)
//        .apiServiceCode(API_NAME)
//        .apiKey(apiKeys.get("API_KEY"))
//        .userKey(user.getUserKey())
//        .build();
//
//    Map<String, Object> body = new HashMap<>();
//
//    body.put("Header", header);
//    body.put("accountNo", accountNo);
//
//    if (refundAmount > 0) {
//      body.put("refundAccountNo", dto.getRefundAccountNo());
//    }
//
//    try {
//      ResponseEntity<Map<String, Object>> response = webClient.post()
//          .uri(API_URL)
//          .contentType(MediaType.APPLICATION_JSON)
//          .bodyValue(body)
//          .retrieve()
//          .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
//          })
//          .block();
//
//      // REC 부분을 Object 타입으로 받기
//      Object recObject = response.getBody().get("REC");
//      ModelMapper modelMapper = new ModelMapper();
//
//      // REC 데이터를 GeneralAccount 엔티티로 변환
//      DeleteAccountResponseDto responseDto = modelMapper.map(recObject,
//          DeleteAccountResponseDto.class);
//
//      if (isForeign) {
//        foreignAccountRepository.deleteByAccountNo(accountNo);
//      } else {
//        generalAccountRepository.deleteByAccountNo(accountNo);
//      }
//
//      if (refundAmount > 0) {
//        GeneralAccount generalAccount = generalAccountRepository.findByAccountNo(
//                responseDto.getRefundAccountNo())
//            .orElseThrow(() -> new RuntimeException(
//                "The RefundAccount Does Not Exist : " + responseDto.getRefundAccountNo()));
//
//      }
//
//      return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    } catch (WebClientResponseException e) {
//      throw e;
//    }
//  }
//
//  public ResponseEntity<ResponseDto> addParticipant(Long accountId,
//      AddParticipantRequestDto requestDto) {
//
//    GeneralAccount generalAccount = generalAccountRepository.findById(accountId).orElseThrow(
//        () -> new IllegalArgumentException("The generalAccountId does not exist: " + accountId));
//
//    GeneralAccount personalAccount =
//        generalAccountRepository.findByAccountNo(requestDto.getParticipantAccountNo()).orElseThrow(
//            () -> new IllegalArgumentException(
//                "The personalAccountId does not exist: " + requestDto.getParticipantAccountNo()));
//    User user = userRepository.findByUserId(requestDto.getParticipantId()).orElseThrow(
//        () -> new IllegalArgumentException(
//            "The participantId does not exist: " + requestDto.getParticipantId()));
//
//    Participant participant = Participant.builder()
//        .isMaster(false)
//        .generalAccount(generalAccount)
//        .personalAccount(personalAccount)
//        .user(user)
//        .build();
//
//    participantRepository.save(participant);
//
//    return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto());
//  }
//
//  public ResponseEntity<ResponseDto> deleteParticipants(Long participantId) {
//
//    participantRepository.deleteById(participantId);
//
//    return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto());
//  }
//
//  public ResponseEntity<ParticipantListResponseDto> getParticipants(Long accountId) {
//
//    List<Participant> participants = participantRepository.findAllByGeneralAccountId(accountId);
//
//    List<ParticipantDto> participantDtoList = participants.stream()
//        .map(ParticipantMapper::toDto)
//        .collect(Collectors.toList());
//
//    ParticipantListResponseDto responseDto = ParticipantListResponseDto.builder()
//        .accountId(accountId)
//        .participants(participantDtoList)
//        .build();
//
//    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//  }

//  public ResponseEntity<List<AccountDto>> getAllGroupInfoByUserId(Long userId) {
//    List<GeneralAccount> allByParticipantUserId = generalAccountRepository.findAllByParticipantUserId(userId);
//
//    List<AccountDto> response = allByParticipantUserId.stream().map(accountMapper::toCreateAccountDto).toList();
//
//    return ResponseEntity.status(HttpStatus.OK).body(response);
//  }
//
//
//  public List<Long> findUserIdsByGeneralAccountId(Long accountId) {
//
//    return participantRepository.findUserIdsByGeneralAccountId(accountId);
//  }
//
//  public Double getBalanceByAccountId(Long accountId) {
//    return generalAccountRepository.findBalanceByAccountId(accountId);
//  }
//
//  public ForeignAccount getForeignAccount(long accountId) {
//
//    return foreignAccountRepository.findById(accountId).get();
//  }
//
//
//  /*
//   * 유저 개인 계좌 전체 조회(기본정보)
//   */
//  public EmailValidationDto getPersonalAccountByEmail(String email) {
//
//    User user = userRepository.findByEmail(email).orElseThrow(
//        () -> new RuntimeException(String.format("loadUserByUsername Failed: %s", email))
//    );
//
//    long userId = user.getUserId();
//    GeneralAccount generalAccount = generalAccountRepository.findFirstByUser_UserIdAndAccountType(
//        userId, AccountType.INDIVIDUAL);
//
//    EmailValidationDto responseDto = EmailValidationDto.builder()
//        .userId(userId)
//        .userName(user.getName())
//        .accountId(generalAccount.getId())
//        .accountNo(generalAccount.getAccountNo())
//        .build();
//
//    return responseDto;
//  }
}
