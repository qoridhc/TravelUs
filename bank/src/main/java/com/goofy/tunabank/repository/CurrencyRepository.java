package com.goofy.tunabank.repository;

import com.goofy.tunabank.domain.Currency;
import com.goofy.tunabank.domain.Enum.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

    Currency findByCurrencyCode(CurrencyType currencyCode);
}
