package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.Participant;
import com.ssafy.soltravel.v2.domain.TravelGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    // 특정 유저가 참여한 모든 그룹 조회
    @Query("SELECT p.group FROM Participant p WHERE p.user.userId = :userId")
    List<TravelGroup> findAllGroupsByUserId(@Param("userId") Long userId);

}
