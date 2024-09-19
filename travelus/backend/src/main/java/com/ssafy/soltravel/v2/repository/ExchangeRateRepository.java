package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, String> {
    ExchangeRate findByCurrencyCode(String currencyCode);
}
