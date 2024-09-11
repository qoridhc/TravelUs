package com.goofy.tunabank.controller;

import com.goofy.tunabank.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exchange")
@Slf4j
public class ExchangeController {

  private final ExchangeService exchangeService;

  /**
   * 테스트용입니다. 삭제할 예정
   */
  @GetMapping
  public ResponseEntity<?> getExchangeRateAll() {

    return ResponseEntity.ok().body(exchangeService.updateExchangeRates());
  }
}
