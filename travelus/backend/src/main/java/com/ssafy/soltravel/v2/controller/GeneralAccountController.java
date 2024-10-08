package com.ssafy.soltravel.v2.controller;

import com.ssafy.soltravel.v2.dto.account.AccountDto;
import com.ssafy.soltravel.v2.dto.account.request.AddMoneyBoxRequestDto;
import com.ssafy.soltravel.v2.dto.account.request.CreateAccountRequestDto;
import com.ssafy.soltravel.v2.dto.account.request.InquireAccountListRequestDto;
import com.ssafy.soltravel.v2.dto.account.request.InquireAccountRequestDto;
import com.ssafy.soltravel.v2.dto.account.request.PasswordValidateRequestDto;
import com.ssafy.soltravel.v2.dto.account.response.PasswordValidateResponseDto;
import com.ssafy.soltravel.v2.dto.moneyBox.MoneyBoxDto;
import com.ssafy.soltravel.v2.service.account.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "GenaralAccount API", description = "일반 계좌 관련 API")
@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class GeneralAccountController {

  private final AccountService accountService;

  // ========= 계좌 CRUD =========
  // 계좌 생성 (모임통장의 경우 외화통장도 자동 생성)
  @Operation(summary = "계좌 생성", description = "일반 계좌(INDIVIDUAL / GROUP) 선택하여 생성. (accountType / accountPassword는 필수), 나머지 값은 모임계좌일경우만 필수.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "계좌 생성 성공", content = @Content(schema = @Schema(implementation = AccountDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)})
  @PostMapping("/createAccount")
  public ResponseEntity<AccountDto> createAccount(@RequestBody CreateAccountRequestDto requestDto) {
    AccountDto generalAccount = accountService.createGeneralAccount(requestDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(generalAccount);
  }

  @Operation(summary = "특정 계좌 조회", description = "계좌 번호를 사용하여 계좌 기본정보를 조회하는 API.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = AccountDto.class))),
      @ApiResponse(responseCode = "404", description = "계좌를 찾을 수 없음", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)})
  @PostMapping("/inquireAccount")
  public ResponseEntity<AccountDto> getByAccountNo(@RequestBody InquireAccountRequestDto requestDto) {

    AccountDto accountDto = accountService.getByAccountNo(requestDto.getAccountNo());

    return ResponseEntity.status(HttpStatus.CREATED).body(accountDto);
  }

  @Operation(summary = "신규 머니박스 추가", description = "특정 계좌에 머니박스를 추가하는 API.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "머니박스 추가 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MoneyBoxDto.class)))),
      @ApiResponse(responseCode = "404", description = "계좌를 찾을 수 없음", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)})

  @PostMapping("/addMoneyBox")
  public ResponseEntity<List<MoneyBoxDto>> addMoneyBox(@RequestBody AddMoneyBoxRequestDto requestDto) {

    List<MoneyBoxDto> accountDto = accountService.addMoneyBox(requestDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(accountDto);
  }

  @Operation(summary = "사용자의 모든 일반(개인/그룹) 계좌 조회", description = "특정 사용자의 모든 일반 계좌를 조회하는 API.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = AccountDto.class))),
      @ApiResponse(responseCode = "404", description = "계좌를 찾을 수 없음", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)})
  @PostMapping("/inquireAccountList")
  public ResponseEntity<List<AccountDto>> getAllByUserId(@RequestBody InquireAccountListRequestDto requestDto) {

    List<AccountDto> accountDtoList = accountService.getAllByUserId(requestDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(accountDtoList);
  }


  @Operation(summary = "계좌 비밀번호 검증", description = "계좌 비밀번호 검증하는 API.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "유효한 비밀번호", content = @Content(schema = @Schema(implementation = AccountDto.class))),
      @ApiResponse(responseCode = "404", description = "계좌를 찾을 수 없음", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)})
  @PostMapping("/validate-password")
  public ResponseEntity<PasswordValidateResponseDto> validatePassword(@RequestBody PasswordValidateRequestDto requestDto) {

    return ResponseEntity.status(HttpStatus.OK).body(accountService.validatePassword(requestDto));
  }

//
//    // 일반 통장 CRUD
//    @Operation(summary = "일반 계좌 삭제", description = "일반 계좌를 삭제하는 API.")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "계좌 삭제 성공", content = @Content(schema = @Schema(implementation = DeleteAccountResponseDto.class))),
//        @ApiResponse(responseCode = "404", description = "계좌를 찾을 수 없음", content = @Content),
//        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
//    })
//    @DeleteMapping("/{accountNo}")
//    public ResponseEntity<DeleteAccountResponseDto> deleteAccount(
//        @Parameter(description = "사용자의 일반 계좌 AccountNo(계좌번호). 해지 계좌 잔액 보유 시 환불 계좌 필수", example = "0889876543210")
//        @PathVariable String accountNo,
//        @RequestBody(required = false) DeleteAccountRequestDto dto
//    ) {
//
//        ResponseEntity<DeleteAccountResponseDto> responseEntity = accountService.deleteAccount(accountNo, false, dto);
//
//        return responseEntity;
//    }
}
