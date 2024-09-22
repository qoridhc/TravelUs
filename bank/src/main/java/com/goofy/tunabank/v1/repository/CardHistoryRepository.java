package com.goofy.tunabank.v1.repository;


import com.goofy.tunabank.v1.domain.CardHistory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CardHistoryRepository {

  private final EntityManager em;

  public Optional<List<CardHistory>> findByTransactionId(String transactionId) {
    List<CardHistory> result = em.createQuery(
        "select ch from CardHistory ch where ch.transactionId = :transactionId"
        , CardHistory.class)
        .setParameter("transactionId", transactionId)
        .getResultList();

    return result.isEmpty() ? Optional.empty() : Optional.of(result);
  }

  public void save(CardHistory cardHistory) {
    em.persist(cardHistory);
  }
}