package com.ssafy.soltravel.controller;

import com.ssafy.soltravel.dto.ResponseDto;
import com.ssafy.soltravel.dto.account.AccountDto;
import com.ssafy.soltravel.dto.account.request.CreateAccountRequestDto;
import com.ssafy.soltravel.dto.account.response.CreateAccountResponseDto;
import com.ssafy.soltravel.dto.account.response.DeleteAccountResponseDto;
import com.ssafy.soltravel.dto.participants.request.AddParticipantRequestDto;
import com.ssafy.soltravel.service.account.AccountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    // ========= 계좌 CRUD =========

    // 계좌 생성
    @PostMapping("/{userId}")
    public ResponseEntity<CreateAccountResponseDto> createAccount(
        @PathVariable Long userId,
        @RequestBody CreateAccountRequestDto dto
    ) {
        CreateAccountResponseDto generalAccount = accountService.createGeneralAccount(userId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(generalAccount);
    }

    @GetMapping("/{userId}/all")
    public ResponseEntity<List<AccountDto>> getAllByUserId(@PathVariable Long userId) {

        ResponseEntity<List<AccountDto>> responseEntity = accountService.getAllByUserId(userId);

        return responseEntity;
    }

    @DeleteMapping("/{accountNo}")
    public ResponseEntity<DeleteAccountResponseDto> deleteAccount(@PathVariable String accountNo) {

        ResponseEntity<DeleteAccountResponseDto> responseEntity = accountService.deleteAccount(accountNo);

        return responseEntity;
    }

    @GetMapping("/{accountNo}")
    public ResponseEntity<AccountDto> getByAccountNo(@PathVariable String accountNo) {

        ResponseEntity<AccountDto> responseEntity = accountService.getByAccountNo(accountNo);

        return responseEntity;
    }

    // ========= 참가자 CRUD =========
    @PostMapping("/{accountId}/participants")
    public ResponseEntity<ResponseDto> addParticipant(
        @PathVariable Long accountId,
        @RequestBody AddParticipantRequestDto requestDto
    ) {

        ResponseEntity<ResponseDto> response = accountService.addParticipant(accountId, requestDto);

        return response;
    }

    // 편하게 계좌 지우는용 -> 쓰지마세용
    @DeleteMapping("/all")
    public void deleteAll() {

        String BASE_URL = "http://localhost:8080/api/v1/account/";

        RestTemplate restTemplate = new RestTemplate();

        String[] accountNumbers = {

        };

        for (String accountNo : accountNumbers) {
            String url = BASE_URL + accountNo;
            restTemplate.delete(url);  // DELETE 요청을 보냅니다.
        }
    }
}
