package com.ssafy.soltravel.repository;

import com.ssafy.soltravel.domain.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, String> {
    ExchangeRate findByCurrencyCode(String currencyCode);
}
