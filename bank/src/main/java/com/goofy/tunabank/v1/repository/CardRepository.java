package com.goofy.tunabank.v1.repository;

import com.goofy.tunabank.v1.domain.Card;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CardRepository {

  private final EntityManager em;

  public void save(Card card) {
    em.persist(card);
  }

}
