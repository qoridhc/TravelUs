package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.PersonalSettlementHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonalSettlementHistoryRepository extends JpaRepository<PersonalSettlementHistory, Long>, PersonalSettlementHistoryRepositoryCustom {

  @Query("SELECT MAX(psh.id) FROM PersonalSettlementHistory psh")
  Long findMaxHistoryId();
}
