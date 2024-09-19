package com.goofy.tunabank.v1.repository;

import com.goofy.tunabank.v1.domain.TransactionHistory;
import com.goofy.tunabank.v1.domain.TransactionHistoryId;
import com.goofy.tunabank.v1.repository.queryDSL.TransactionHistoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionHistoryRepository extends
    JpaRepository<TransactionHistory, TransactionHistoryId>, TransactionHistoryCustomRepository {

  @Query("SELECT MAX(t.id) FROM TransactionHistory t")
  Long findMaxAccountId();
}
