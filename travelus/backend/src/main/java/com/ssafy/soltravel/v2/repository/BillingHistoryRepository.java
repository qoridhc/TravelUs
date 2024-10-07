package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.BillingHistory;
import com.ssafy.soltravel.v2.domain.BillingHistoryDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingHistoryRepository extends JpaRepository<BillingHistory, Long>{

  @Query("SELECT b FROM BillingHistoryDetail b WHERE b.participant.user.userId = :userId")
  List<BillingHistoryDetail> findByUserId(@Param("userId") Long userId);
}
