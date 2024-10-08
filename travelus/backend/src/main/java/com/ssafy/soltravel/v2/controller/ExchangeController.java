package com.ssafy.soltravel.v2.controller;

import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateRegisterRequestDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateResponseDto;
import com.ssafy.soltravel.v2.dto.exchange.TargetRateUpdateRequestDto;
import com.ssafy.soltravel.v2.service.exchange.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exchange")
@Slf4j
@Tag(name = "Exchange API", description = "환전 관련 API")
public class ExchangeController {

  private final ExchangeService exchangeService;

  /**
   * 환율 전체 조회
   */
  @GetMapping("/rate")
  @Operation(summary = "환율 전체 조회", description = "전체 통화의 실시간 환율을 조회합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "성공적으로 환율을 조회했습니다.", content = @Content(schema = @Schema(implementation = ExchangeRateResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content),
      @ApiResponse(responseCode = "404", description = "요청한 통화를 찾을 수 없습니다.", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류입니다.", content = @Content)})
  public ResponseEntity<List<ExchangeRateResponseDto>> getExchangeRateAll() {

    return ResponseEntity.ok().body(exchangeService.getExchangeRateAll());
  }

  /**
   * 환율 단건 조회
   */
  @GetMapping("/rate/{currencyCode}")
  @Operation(summary = "환율 단건 조회", description = "특정 통화의 실시간 환율을 조회합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "성공적으로 환율을 조회했습니다.", content = @Content(schema = @Schema(implementation = ExchangeRateResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content),
      @ApiResponse(responseCode = "404", description = "요청한 통화를 찾을 수 없습니다.", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류입니다.", content = @Content)})
  public ResponseEntity<ExchangeRateResponseDto> getExchangeRate(
      @Parameter(description = "조회할 통화의 코드", example = "USD") @PathVariable String currencyCode) {

    return ResponseEntity.ok().body(exchangeService.getExchangeRate(currencyCode));
  }

  /**
   * 희망 환율 저장
   */
  @PostMapping("/rate/target-rate")
  @Operation(summary = "희망 환율 설정", description = "희망 환율을 설정합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "성공적으로 환율을 저장했습니다.", content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류입니다.", content = @Content)})
  public ResponseEntity<String> setExchangeRate(
      @RequestBody ExchangeRateRegisterRequestDto requestDto) {
    exchangeService.setPreferenceRate(requestDto,false,-1);
    return ResponseEntity.ok().body("register success");
  }

  /**
   * 희망 환율 수정
   */
  @PutMapping("/rate/target-rate")
  @Operation(summary = "희망 환율 수정", description = "희망 환율을 수정합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "성공적으로 환율을 저장했습니다.", content = @Content(schema = @Schema(implementation = ExchangeRateRegisterRequestDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류입니다.", content = @Content)})
  public ResponseEntity<String> updateExchangeRate(
      @RequestBody TargetRateUpdateRequestDto requestDto) {
    exchangeService.updateTargetRate(requestDto);
    return ResponseEntity.ok().body("update success");
  }
}
