package com.ssafy.soltravel.repository;

import com.ssafy.soltravel.domain.Participant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

  @Query("SELECT p.user.userId FROM Participant p WHERE p.generalAccount.id = :generalAccountId")
  List<Long> findUserIdsByGeneralAccountId(@Param("generalAccountId") Long generalAccountId);
}
