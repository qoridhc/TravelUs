package com.goofy.tunabank.v1.repository;

import com.goofy.tunabank.v1.domain.Key;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KeyRepository {

  private final EntityManager em;

  public Optional<Key> findByKeyValue(String keyValue) {
    List<Key> result = em.createQuery("select k from Key k where k.value=:keyValue")
        .setParameter("keyValue", keyValue)
        .getResultList();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public void save(Key key) {
    em.persist(key);
  }
}
