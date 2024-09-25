package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.TravelGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupRepository extends JpaRepository<TravelGroup, Long> {

    @Query("SELECT p.user.userId FROM Participant p WHERE p.group.groupAccountNo = :accountNo AND p.isMaster = true")
    Long findMasterUserIdByAccountNo(@Param("accountNo") String accountNo);


}
