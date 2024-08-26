package com.ssafy.soltravel.repository;

import com.ssafy.soltravel.domain.LatestRate;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LatestRateRepository extends CrudRepository<LatestRate, Long> {

  @Query("SELECT lr FROM LatestRate lr WHERE lr.currency.currencyCode = :currency")
  List<LatestRate> findLatestRatesByCurrencyId(@Param("currency") String currency);
}
