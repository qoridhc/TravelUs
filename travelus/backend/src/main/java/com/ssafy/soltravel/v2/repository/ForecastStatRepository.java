package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.ForecastStat;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ForecastStatRepository {

  private final EntityManager em;

  public void save(ForecastStat stat){
    em.persist(stat);
  }
}
