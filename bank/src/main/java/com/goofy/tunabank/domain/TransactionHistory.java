package com.goofy.tunabank.domain;

import com.goofy.tunabank.domain.Enum.TransactionType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class TransactionHistory {

  //거래 기록 id
  @Id
  @Column(name = "transaction_history_id")
  private Long Id;

  //통장
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "account_id")
  private Account account;

  //가맹점

  //거래 종류
  @Column(name = "transaction_type")
  private TransactionType transactionType;

  //거래 일시
  @Column(name = "transaction_at")
  private LocalDateTime transactionAt;

  //금액
  private double amount;

  //잔액
  private double balance;
}
