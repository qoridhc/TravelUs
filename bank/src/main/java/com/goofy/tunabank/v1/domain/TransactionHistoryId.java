package com.goofy.tunabank.v1.domain;

import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionHistoryId {

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
    TransactionHistoryId transactionHistoryId = (TransactionHistoryId) obj;
    return Objects.equals(id, transactionHistoryId.id) && transactionType == transactionHistoryId.transactionType;
  }
}
