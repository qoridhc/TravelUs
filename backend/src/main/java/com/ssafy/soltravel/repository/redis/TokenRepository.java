package com.ssafy.soltravel.repository.redis;


import com.ssafy.soltravel.domain.redis.RedisToken;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<RedisToken, Long> {

}

