package com.goofy.tunabank.v1.domain;

import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(TransactionHistoryId.class)
public class TransactionHistory {

  //거래 기록 id
  @Id
  @Column(name = "transaction_history_id")
  private Long id;

  //거래 종류
  @Id
  @Enumerated(EnumType.STRING)
  @Column(name = "transaction_type")
  private TransactionType transactionType;

  //돈통 id
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "money_box_id")
  private MoneyBox moneyBox;

  //상대 계좌 번호
  @JoinColumn(name = "transaction_account_no")
  private String transactionAccountNo;

  //거래 일시
  @Column(name = "transaction_at")
  private LocalDateTime transactionAt;

  //금액
  private double amount;

  //잔액
  private double balance;

  //메모
  private String summary;

  /**
   * 엔티티 생성 메서드
   */
  public static TransactionHistory createTransactionHistory(Long nextId,
      TransactionType transactionType, MoneyBox moneyBox, String transactionAccountNo,
      LocalDateTime transactionAt, double amount, double balance, String summary) {

    TransactionHistory transactionHistory = new TransactionHistory();
    transactionHistory.setId(nextId);
    transactionHistory.setTransactionType(transactionType);
    transactionHistory.setMoneyBox(moneyBox);
    transactionHistory.setTransactionAccountNo(transactionAccountNo);
    transactionHistory.setTransactionAt(transactionAt);
    transactionHistory.setAmount(amount);
    transactionHistory.setBalance(balance);
    transactionHistory.setSummary(summary);
    return transactionHistory;

  }
}