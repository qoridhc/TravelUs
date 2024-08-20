package com.ssafy.soltravel.controller;

import com.ssafy.soltravel.dto.exchange.ExchangeRateResponseDto;
import com.ssafy.soltravel.service.exchange.ExchangeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exchange")
@Slf4j
public class ExchangeController {


  private final ExchangeService exchangeService;

  /**
   * 현재 환율 조회하는 메서드
   */
  public ResponseEntity<List<ExchangeRateResponseDto>> getExchangeRate() {

    return ResponseEntity.ok(exchangeService.getExchangeRate());
  }
}
