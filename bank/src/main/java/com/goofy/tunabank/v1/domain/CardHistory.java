package com.goofy.tunabank.v1.domain;

import com.goofy.tunabank.v1.dto.card.CardPaymentRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
@Table(name = "card_history")
public class CardHistory {

  @Id
  @Column(name = "card_history_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "currency_id")
  private Currency currency;

  @Column
  private LocalDateTime transactionAt;

  @Column
  private Double amount;

  @Column
  private Double balance;

  @Column
  private Double wonAmount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "merchant_id")
  private Merchant merchant;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "card_id")
  private Card card;

  @Column(name = "applied_exchange_rate")
  private Double exchangeRate;

  @Column
  private String transactionId;

  /*
  * 생성 메서드
  */
  public static CardHistory createCardHistory(
      Currency currency, Double amount, Double balance, Double wonAmount, Merchant merchant, Card card, String transactionId
  ) {

    CardHistory cardHistory = new CardHistory();
    cardHistory.currency = currency;
    cardHistory.amount = amount;
    cardHistory.balance = balance;
    cardHistory.wonAmount = wonAmount;
    cardHistory.merchant = merchant;
    cardHistory.card = card;
    cardHistory.exchangeRate = currency.getExchangeRate();
    cardHistory.transactionId = transactionId;
    cardHistory.transactionAt = LocalDateTime.now();
    return cardHistory;
  }

  public static CardHistory createCardHistory(
      Card card, MoneyBox moneyBox, Merchant merchant, CardPaymentRequestDto request, Double wonAmount
  ) {

    CardHistory cardHistory = new CardHistory();
    Currency currency = moneyBox.getCurrency();

    cardHistory.currency = currency;
    cardHistory.amount = request.getPaymentBalance();
    cardHistory.balance = moneyBox.getBalance();
    cardHistory.wonAmount = wonAmount;
    cardHistory.merchant = merchant;
    cardHistory.card = card;
    cardHistory.exchangeRate = currency.getExchangeRate();
    cardHistory.transactionId = request.getTransactionId();
    cardHistory.transactionAt = LocalDateTime.now();
    return cardHistory;
  }
}
