package com.ssafy.soltravel.repository.redis;


import com.ssafy.soltravel.domain.redis.RedisPhone;
import org.springframework.data.repository.CrudRepository;

public interface PhoneRepository extends CrudRepository<RedisPhone, String> {

}

