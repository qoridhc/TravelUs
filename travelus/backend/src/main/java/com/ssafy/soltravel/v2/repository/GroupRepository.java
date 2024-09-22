package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.TravelGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<TravelGroup, Long> {
}
