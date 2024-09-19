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
@Builder
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

  //거래 일시
  @Column(name = "transaction_at")
  private LocalDateTime transactionAt;

  //금액
  private double amount;

  //잔액
  private double balance;

  //메모
  private String summary;
}