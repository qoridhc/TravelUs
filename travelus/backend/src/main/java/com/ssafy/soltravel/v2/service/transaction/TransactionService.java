package com.ssafy.soltravel.v2.service.transaction;

import com.ssafy.soltravel.v2.common.Header;
import com.ssafy.soltravel.v2.domain.Enum.TransactionType;
import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.dto.transaction.request.MoneyBoxTransferRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransactionHistoryRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransferRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.response.TransactionResponseDto;
import com.ssafy.soltravel.v2.dto.transaction.response.TransferHistoryResponseDto;
import com.ssafy.soltravel.v2.exception.user.UserNotFoundException;
import com.ssafy.soltravel.v2.repository.UserRepository;
import com.ssafy.soltravel.v2.util.LogUtil;
import com.ssafy.soltravel.v2.util.SecurityUtil;
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

    private final Map<String, String> apiKeys;

    private final UserRepository userRepository;

    private final SecurityUtil securityUtil;
    private final WebClientUtil webClientUtil;

    private final ModelMapper modelMapper;

    private final String BASE_URL = "/transaction/";

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
        MoneyBoxTransferRequestDto requestDto, boolean isAuto, long userId) {

        return processMoneyBoxTransfer("moneybox", requestDto, isAuto, userId);
    }

    /**
     * 입출금 공통 요청 처리 메서드
     */
    private ResponseEntity<TransactionResponseDto> processTransaction(String apiName,
        TransactionRequestDto requestDto) {

        // 유저 조회
        User user = securityUtil.getUserByToken();

        String API_URL = BASE_URL + apiName;

        Header header = Header.builder()
            .apiKey(apiKeys.get("API_KEY"))
            .userKey(user.getUserKey()).build();

        Map<String, Object> body = new HashMap<>();
        body.put("Header", header);
        body.put("accountNo", requestDto.getAccountNo());
        body.put("accountPassword", requestDto.getAccountPassword());
        body.put("currencyCode", requestDto.getCurrencyCode());
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

        User user = securityUtil.getUserByToken();

        String API_URL = BASE_URL + "transfer/" + apiName;

        Header header = Header.builder()
            .apiKey(apiKeys.get("API_KEY"))
            .userKey(user.getUserKey()).build();

        Map<String, Object> body = new HashMap<>();
        body.put("Header", header);
        body.put("transferType", requestDto.getTransferType());
        body.put("withdrawalAccountNo", requestDto.getWithdrawalAccountNo());
        body.put("accountPassword", requestDto.getAccountPassword());
        body.put("depositAccountNo", requestDto.getDepositAccountNo());
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
        MoneyBoxTransferRequestDto requestDto, boolean isAuto, long userId) {

        String API_URL = BASE_URL + "transfer/" + apiName;
        if (isAuto) {//자동환전이라면

            API_URL += "/auto";
        } else {

            userId = securityUtil.getCurrentUserId();
        }
        long finalUserId = userId;

        User user = userRepository.findByUserId(finalUserId)
            .orElseThrow(() -> new UserNotFoundException(finalUserId));

        Header header = Header.builder()
            .apiKey(apiKeys.get("API_KEY"))
            .userKey(user.getUserKey()).build();

        Map<String, Object> body = new HashMap<>();
        body.put("Header", header);
        body.put("transferType", requestDto.getTransferType());
        body.put("accountNo", requestDto.getAccountNo());
        body.put("accountPassword", requestDto.getAccountPassword());
        body.put("sourceCurrencyCode", requestDto.getSourceCurrencyCode());
        body.put("targetCurrencyCode", requestDto.getTargetCurrencyCode());
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

        User user = securityUtil.getUserByToken();

        String API_URL = BASE_URL + "history";

        Header header = Header.builder()
            .apiKey(apiKeys.get("API_KEY"))
            .userKey(user.getUserKey()).build();

        LogUtil.info("requestDto: ", requestDto);
        Map<String, Object> body = new HashMap<>();
        body.put("Header", header);
        TransactionType transactionType = requestDto.getTransactionType();
        if (transactionType != null) {
            body.put("transactionType", transactionType);
        }
        body.put("accountNo", requestDto.getAccountNo());
        body.put("currencyCode", requestDto.getCurrencyCode());
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
