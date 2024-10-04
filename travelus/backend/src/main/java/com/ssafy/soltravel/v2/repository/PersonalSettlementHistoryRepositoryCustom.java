package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.Enum.SettlementStatus;
import com.ssafy.soltravel.v2.domain.PersonalSettlementHistory;
import java.util.List;
import java.util.Optional;

public interface PersonalSettlementHistoryRepositoryCustom {
  Optional<List<PersonalSettlementHistory>> findByUserIdAndSettlementStatus(Long userId, SettlementStatus status);
}
