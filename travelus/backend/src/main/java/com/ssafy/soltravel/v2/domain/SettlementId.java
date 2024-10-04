package com.ssafy.soltravel.v2.domain;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SettlementId {

  private Long id;
  private Participant participant;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SettlementId that = (SettlementId) o;
    return Objects.equals(id, that.id) && Objects.equals(participant, that.participant);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, participant);
  }
}
