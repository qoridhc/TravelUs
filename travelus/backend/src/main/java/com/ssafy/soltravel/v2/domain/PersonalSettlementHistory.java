package com.ssafy.soltravel.v2.domain;

import com.ssafy.soltravel.v2.domain.Enum.SettlementStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(SettlementId.class)
public class PersonalSettlementHistory {

  @Id
  @Column(name = "personal_settlement_history_id")
  private Long id;

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "participant_id")
  private Participant participant;

  //정산 금액
  private double amount;

  //남은 금액
  private double remainingAmount;

  //정산 여부
  @Enumerated(EnumType.STRING)
  private SettlementStatus isSettled;

  //생성 일시
  private LocalDateTime createdAt;

  public static PersonalSettlementHistory createPersonalSettlementHistory(long id, Participant participant, double amount, LocalDateTime now) {
    PersonalSettlementHistory personalSettlementHistory = new PersonalSettlementHistory();
    personalSettlementHistory.id = id;
    personalSettlementHistory.participant = participant;
    personalSettlementHistory.amount = amount;
    personalSettlementHistory.remainingAmount = amount;
    personalSettlementHistory.createdAt = now;
    personalSettlementHistory.isSettled = SettlementStatus.NOT_COMPLETED;
    return personalSettlementHistory;
  }
}
