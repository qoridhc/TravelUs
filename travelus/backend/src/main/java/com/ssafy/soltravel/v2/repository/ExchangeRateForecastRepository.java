package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.ExchangeRateForecast;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ExchangeRateForecastRepository {

  private final EntityManager em;

  public void save(ExchangeRateForecast forecast) {
    em.persist(forecast);
  }

  public void save(List<ExchangeRateForecast> list) {
    for (ExchangeRateForecast forecast : list) {
      save(forecast);
    }
  }
}
