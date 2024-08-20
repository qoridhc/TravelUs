package com.ssafy.soltravel.controller;

import com.ssafy.soltravel.dto.exchange.ExchangeRateDto;
import com.ssafy.soltravel.dto.exchange.ExchangeRateRequestDto;
import com.ssafy.soltravel.dto.exchange.ExchangeRateResponseDto;
import com.ssafy.soltravel.service.exchange.ExchangeService;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
  public ResponseEntity<ExchangeRateResponseDto> getExchangeRate(@RequestBody ExchangeRateRequestDto requestDto) {

    return ResponseEntity.ok(exchangeService.getExchangeRate(requestDto.getCurrency()));
  }

  /**
   * TODO: 최근 3개월 환율 조회 -> 수출입 은행 API
   */

}
