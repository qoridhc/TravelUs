package com.goofy.tunabank.v1.repository;

import com.goofy.tunabank.v1.domain.MoneyBox;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MoneyBoxRepository extends JpaRepository<MoneyBox,Long> {

  @Query("SELECT mb FROM MoneyBox mb " +
      "JOIN FETCH mb.currency c " +
      "JOIN FETCH mb.account a " +
      "WHERE a.id = :accountId " +
      "AND c.id = :currencyId")
  Optional<MoneyBox> findMoneyBoxByAccountAndCurrency(@Param("accountId") Long accountId, @Param("currencyId") int currencyId);
}
