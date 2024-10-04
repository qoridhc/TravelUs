package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.PersonalSettlementHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonalSettlementHistoryRepository extends JpaRepository<PersonalSettlementHistory, Long>, PersonalSettlementHistoryRepositoryCustom {

  @Query("SELECT MAX(psh.id) FROM PersonalSettlementHistory psh")
  Long findMaxHistoryId();


  @Query("SELECT psh FROM PersonalSettlementHistory psh " +
      "WHERE psh.id = :id AND psh.participant.id = :participantId")
  Optional<PersonalSettlementHistory> findByIdAndParticipantId(
      @Param("id") Long id,
      @Param("participantId") Long participantId
  );


  @Query("SELECT psh FROM PersonalSettlementHistory psh " +
      "JOIN psh.group g " +
      "WHERE g.groupId = :groupId " +
      "GROUP BY psh.id, psh.participant.id, psh.amount, " +
      "psh.createdAt, psh.group.groupId, psh.isSettled, psh.remainingAmount")
  List<PersonalSettlementHistory> findByGroupIdGroupedById(@Param("groupId") Long groupId);
}
