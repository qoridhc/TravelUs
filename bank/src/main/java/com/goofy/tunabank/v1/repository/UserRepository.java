package com.goofy.tunabank.v1.repository;

import com.goofy.tunabank.v1.domain.User;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

  private final EntityManager em;

  public Optional<User> findById(Long id) {
    return Optional.ofNullable(em.find(User.class, id));
  }

  public Optional<User> findByEmail(String email) {
    List<User> result = em.createQuery("select u from User u where u.email = :email")
        .setParameter("email", email)
        .getResultList();
    return result.stream().findFirst();
  }

  public Optional<User> findByUserKey(String userKey){
    List<User> result = em.createQuery("SELECT u FROM User u JOIN FETCH u.keys k WHERE k.value = :keyValue")
        .setParameter("keyValue", userKey)
        .getResultList();
    return result.stream().findFirst();
  }

  public void save(User user) {
    em.persist(user);
  }
}
