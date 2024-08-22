package com.ssafy.soltravel.controller;

import com.ssafy.soltravel.dto.exchange.ExchangeRateRegisterRequestDto;
import com.ssafy.soltravel.dto.exchange.ExchangeRateRequestDto;
import com.ssafy.soltravel.dto.exchange.ExchangeRateResponseDto;
import com.ssafy.soltravel.dto.exchange.ExchangeRequestDto;
import com.ssafy.soltravel.dto.exchange.ExchangeResponseDto;
import com.ssafy.soltravel.service.exchange.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exchange")
@Slf4j
public class ExchangeController {


  private final ExchangeService exchangeService;

  /**
   * 실시간 환율 조회
   */
  @GetMapping
  public ResponseEntity<ExchangeRateResponseDto> getExchangeRate(
      @RequestBody ExchangeRateRequestDto requestDto) {

    exchangeService.ScheduledGetExchangeRate();
    return ResponseEntity.ok().body(exchangeService.getExchangeRate(requestDto.getCurrency()));
  }

  /**
   * 환전 실행
   */
  @PostMapping
  public ResponseEntity<ExchangeResponseDto> exchange(@RequestBody ExchangeRequestDto requestDto) {

    return ResponseEntity.ok().body(exchangeService.executeExchange(requestDto));
  }

  /**
   * 원하는 환율 저장
   */
  @PostMapping("/register")
  public ResponseEntity<?> setExchangeRate(
      @RequestBody ExchangeRateRegisterRequestDto requestDto) {

    exchangeService.setPreferenceRate(requestDto);
    return ResponseEntity.ok().body("register success");
  }

  /**
   * TODO: 최근 환율 조회
   */

}
