package com.ssafy.soltravel.v2.repository.redis;

import com.ssafy.soltravel.v2.domain.redis.PreferenceRate;
import org.springframework.data.repository.CrudRepository;

public interface PreferenceRateRepository extends CrudRepository<PreferenceRate, String> {
}
