package com.ssafy.soltravel.v2.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.ExchangeRate;
import com.ssafy.soltravel.v2.domain.QExchangeRate;
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

  public void save(ExchangeRate forecast) {
    em.persist(forecast);
  }

  public void save(List<ExchangeRate> list) {
    for (ExchangeRate forecast : list) {
      save(forecast);
    }
  }
  public Optional<ExchangeRate> findByDateAndCurrency(LocalDate date, CurrencyType type) {
    List<ExchangeRate> result = em.createQuery(
        "select er from ExchangeRate er " +
            "where er.date = :date and " +
            "er.currency = :type"
        , ExchangeRate.class
        )
        .setParameter("date", date)
        .setParameter("type", type)
        .getResultList();
    return result.stream().findFirst();
  }

  public Optional<List<ExchangeRate>> findByPeriodAndCurrency(LocalDate start, LocalDate end, CurrencyType cType) {
    QExchangeRate q = new QExchangeRate("q");
    List<ExchangeRate> result = queryFactory.selectFrom(q)
        .where(
            dateBetween(q, start, end),
            currencyEq(q, cType)
        )
        .orderBy(q.date.asc())
        .fetch();
    return Optional.ofNullable(result.isEmpty() ? null : result);
  }

  private BooleanExpression dateBetween(QExchangeRate qerf, LocalDate start, LocalDate end) {
    return qerf.date.between(start, end);
  }

  private BooleanExpression currencyEq(QExchangeRate qerf, CurrencyType currencyCond) {
    if (currencyCond == null) {
      return null;
    }
    return qerf.currency.eq(currencyCond);
  }
}
