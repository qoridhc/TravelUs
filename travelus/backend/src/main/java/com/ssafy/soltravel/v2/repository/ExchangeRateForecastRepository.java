package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.ExchangeRateForecast;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

  public Optional<ExchangeRateForecast> findByDateAndCurrency(LocalDate date, CurrencyType type) {
    List<ExchangeRateForecast> result = em.createQuery(
        "select erf from ExchangeRateForecast erf " +
            "where erf.date = :date and " +
            "erf.currency = :type"
        , ExchangeRateForecast.class
        )
        .setParameter("date", date)
        .setParameter("type", type)
        .getResultList();
    return result.stream().findFirst();
  }
}
