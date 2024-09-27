package com.ssafy.soltravel.v2.controller;

import com.ssafy.soltravel.v2.dto.ResponseDto;
import com.ssafy.soltravel.v2.dto.exchange.forecast.ExchangeRateSaveRequestDto;
import com.ssafy.soltravel.v2.service.exchange.ExchangeRateForecastService;
import com.ssafy.soltravel.v2.service.exchange.ExchangeService;
import com.ssafy.soltravel.v2.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange/forecast")
@RequiredArgsConstructor
public class ExchangeRateForecastController {

  private final ExchangeRateForecastService exchangeRateForecastService;

  @PostMapping("/save")
  public ResponseEntity<?> saveExchangeRatePred(@RequestBody ExchangeRateSaveRequestDto request){
    LogUtil.info("환율 예측값 저장 요청", request);
    int response = exchangeRateForecastService.saveExchangeRate(request);
    return ResponseEntity.ok(new ResponseDto(String.format("예측값(%d) 저장 완료", response)));
  }
}
