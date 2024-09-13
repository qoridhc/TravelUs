package com.goofy.tunabank.domain;

import com.goofy.tunabank.domain.Enum.TransactionType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
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
public class TransferHistory {

  //이체 기록 id
  @Id
  @Column(name = "transfer_id")
  private Long id;

  //거래 종류
  @Id
  @Enumerated(EnumType.STRING)
  @Column(name = "transaction_type")
  private TransactionType transactionType;

  //통장
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumns({
      @JoinColumn(name = "account_id", referencedColumnName = "account_id"),
      @JoinColumn(name = "account_type", referencedColumnName = "account_type")
  })
  private Account account;

  //거래 일시
  @Column(name = "transaction_at")
  private LocalDateTime transactionAt;

  //금액
  private double amount;

  //잔액
  private double balance;
}
