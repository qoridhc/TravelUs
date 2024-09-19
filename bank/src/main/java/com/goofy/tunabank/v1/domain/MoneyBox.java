package com.goofy.tunabank.v1.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MoneyBox {

  //돈통 id
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "money_box_id")
  private Long id;

  //통장 id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id")
  private Account account;

  //통화 id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "currency_id")
  private Currency currency;

  //잔액
  private double balance;

  //거래 기록
  @OneToMany(mappedBy = "moneyBox", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TransactionHistory> transactionHistories;
}
