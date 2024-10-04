package com.ssafy.soltravel.v2.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.soltravel.v2.domain.Enum.SettlementStatus;
import com.ssafy.soltravel.v2.domain.PersonalSettlementHistory;
import com.ssafy.soltravel.v2.domain.QParticipant;
import com.ssafy.soltravel.v2.domain.QPersonalSettlementHistory;
import com.ssafy.soltravel.v2.domain.QUser;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PersonalSettlementHistoryRepositoryImpl implements PersonalSettlementHistoryRepositoryCustom{

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<List<PersonalSettlementHistory>> findByUserIdAndSettlementStatus(Long userId, SettlementStatus status) {
    QPersonalSettlementHistory personalSettlementHistory = QPersonalSettlementHistory.personalSettlementHistory;
    QParticipant participant = QParticipant.participant;
    QUser user = QUser.user;

    List<PersonalSettlementHistory> results = queryFactory
        .selectFrom(personalSettlementHistory)
        .join(participant.user, user)
        .where(user.userId.eq(userId)
            .and(status == null ? null : personalSettlementHistory.isSettled.eq(status)))
        .fetch();

    return results.isEmpty() ? Optional.empty() : Optional.of(results);
  }
}
