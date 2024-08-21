package com.ssafy.soltravel.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.soltravel.domain.QUser;
import com.ssafy.soltravel.domain.User;
import com.ssafy.soltravel.dto.user.UserSearchRequestDto;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  public Optional<User> findByName(String name) {
    List<User> result = em.createQuery("select u from User u where u.name = :name", User.class)
        .setParameter("name", name)
        .getResultList();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public Optional<User> findByEmail(String email) {
    List<User> result = em.createQuery("select u from User u where u.email = :email", User.class)
        .setParameter("email", email)
        .getResultList();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public Optional<User> findByUserId(Long userId) {
    List<User> result = em.createQuery("select u from User u where u.id = :id", User.class)
        .setParameter("id", userId)
        .getResultList();
    return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
  }

  public void save(User user) {
    em.persist(user);
  }

  public List<User> findAll(UserSearchRequestDto searchDto) {
    QUser qUser = new QUser("u");

    return queryFactory
        .selectFrom(qUser)
        .where(
            quizIdEq(qUser, searchDto.getUserId())
        )
        .limit(1000)
        .fetch();
  }

  private BooleanExpression quizIdEq(QUser user, Long userIdCond) {
    if (userIdCond == null) {
      return null;
    }
    return user.id.eq(userIdCond);
  }
}
