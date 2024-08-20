package com.ssafy.soltravel.service.exchange;

import com.ssafy.soltravel.dto.exchange.ExchangeRateResponseDto;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeService {

  @Value("${exchange.api-key}")
  private String authKey;

  private final WebClient webClient;
  private final String BASE_URL = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON";

  public List<ExchangeRateResponseDto> getExchangeRate() {

    String searchdate = new SimpleDateFormat("yyyyMMdd").format(new Date());
    String data = "AP01";
    BigDecimal exchangeRate = null;

    return null;
  }
}
