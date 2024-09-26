package com.ssafy.soltravel.v2.domain;


import com.ssafy.soltravel.v1.domain.ExchangeRate;
import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.Getter;

@Entity
@Getter
public class ExchangeRateForecast {

  @Id
  @Column(name = "exchange_rate_forecast_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private Double rate;

  @Column
  private CurrencyType currency;

  @Column
  private LocalDate baseDate;

  @Column
  private Long date;


  public static ExchangeRateForecast create(LocalDate baseDate, Long date, CurrencyType currency, Double rate) {
    ExchangeRateForecast forecast = new ExchangeRateForecast();
    forecast.baseDate = baseDate;
    forecast.date = date;
    forecast.currency = currency;
    forecast.rate = rate;
    return forecast;
  }
}
