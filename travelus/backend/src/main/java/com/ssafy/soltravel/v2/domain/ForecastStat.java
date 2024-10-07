package com.ssafy.soltravel.v2.domain;


import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.Enum.Trend;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;

@Entity
@Getter
@Table(
    name = "forecast_stat",
    uniqueConstraints = {
        @jakarta.persistence.UniqueConstraint(columnNames = {"date", "currency"})
    }
)
public class ForecastStat {
  @Id
  @Column(name = "exchange_rate_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDate date;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private CurrencyType currency;

  @Column
  @Enumerated(EnumType.STRING)
  private Trend trend;

  @Column
  private Double average;

  @Column
  private Double min;

  @Column
  private Double max;

  @Column
  private Double p10;

  @Column
  private Double p20;

  @Column
  private Double p30;

  @Column
  private Double p40;

  @Column
  private Double p50;

  @Column
  private Double p60;

  @Column
  private Double p70;

  @Column
  private Double p80;

  @Column
  private Double p90;

  @Column
  private Double p100;
}
