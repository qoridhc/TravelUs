package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.BillingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingHistoryRepository extends JpaRepository<BillingHistory, Long>{

}
