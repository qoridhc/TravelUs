package com.ssafy.soltravel.repository.redis;

import com.ssafy.soltravel.domain.redis.PreferenceRate;
import org.springframework.data.repository.CrudRepository;

public interface PreferenceRateRepository extends CrudRepository<PreferenceRate, String> {
}
