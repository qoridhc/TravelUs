package com.goofy.tunabank.domain;

import com.goofy.tunabank.domain.Enum.TransactionType;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransferId implements Serializable {

  private Long id;
  private TransactionType transactionType;

  @Override
  public int hashCode() {
    return Objects.hash(id,transactionType);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    TransferId transferId = (TransferId) obj;
    return Objects.equals(id, transferId.id) && transactionType == transferId.transactionType;
  }
}
