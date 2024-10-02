package com.ssafy.soltravel.v2.controller;

import com.ssafy.soltravel.v2.dto.ResponseDto;
import com.ssafy.soltravel.v2.dto.exchange.forecast.ExchangeRateForecastResponseDto;
import com.ssafy.soltravel.v2.dto.exchange.forecast.ExchangeRateForecastResponseDto.ExchangeRateData;
import com.ssafy.soltravel.v2.dto.exchange.forecast.ExchangeRateSaveRequestDto;
import com.ssafy.soltravel.v2.service.exchange.ExchangeRateForecastService;
import com.ssafy.soltravel.v2.util.LogUtil;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange/forecast")
@RequiredArgsConstructor
public class ExchangeRateForecastController {

  private final ExchangeRateForecastService exchangeRateForecastService;

  @PostMapping("/update")
  public ResponseEntity<?> saveExchangeRatePred(@RequestBody ExchangeRateSaveRequestDto request){
    LogUtil.info("환율 예측값 저장(수정) 요청", request);
    int response = exchangeRateForecastService.parsePredExchangeRates(request);
    return ResponseEntity.ok(new ResponseDto(String.format("예측값(%d) 저장 완료", response)));
  }

  @PostMapping("/save/all")
  public ResponseEntity<?> saveExchangeRateHistory(@RequestBody ExchangeRateSaveRequestDto request){
    LogUtil.info("전체 환율값 저장 요청", request);
    int response = exchangeRateForecastService.parseExchangeRates(request);
    return ResponseEntity.ok(new ResponseDto(String.format("예측값(%d) 저장 완료", response)));
  }

  @GetMapping("/{currencyCode}")
  public ResponseEntity loadExchangeRate(@PathVariable(name = "currencyCode") String currencyCode){
    LogUtil.info("환율 조회 요청", currencyCode);
    Map<String, ExchangeRateData> response =
        exchangeRateForecastService.findRatesByCurrencyCode(currencyCode);
    return ResponseEntity.ok(response);
  }
}
