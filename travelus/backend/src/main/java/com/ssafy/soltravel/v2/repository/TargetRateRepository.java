package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.TargetRate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TargetRateRepository extends JpaRepository<TargetRate, Long> {

  @Query("SELECT t FROM TargetRate t WHERE t.group.groupId = :groupId")
  Optional<TargetRate> findByGroupId(@Param("groupId") Long groupId);
}
