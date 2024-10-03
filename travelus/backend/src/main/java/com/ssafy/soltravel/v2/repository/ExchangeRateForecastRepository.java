package com.ssafy.soltravel.v2.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.ExchangeRateForecast;
import com.ssafy.soltravel.v2.domain.QExchangeRateForecast;
import com.ssafy.soltravel.v2.domain.QUser;
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
  private final JPAQueryFactory queryFactory;

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

  public Optional<List<ExchangeRateForecast>> findByPeriodAndCurrency(LocalDate start, LocalDate end, CurrencyType cType) {
    QExchangeRateForecast q = new QExchangeRateForecast("q");
    List<ExchangeRateForecast> result = queryFactory.selectFrom(q)
        .where(
            dateBetween(q, start, end),
            currencyEq(q, cType)
        )
        .orderBy(q.date.asc())
        .fetch();
    return Optional.ofNullable(result.isEmpty() ? null : result);
  }

  private BooleanExpression dateBetween(QExchangeRateForecast qerf, LocalDate start, LocalDate end) {
    return qerf.date.between(start, end);
  }

  private BooleanExpression currencyEq(QExchangeRateForecast qerf, CurrencyType currencyCond) {
    if (currencyCond == null) {
      return null;
    }
    return qerf.currency.eq(currencyCond);
  }
}
