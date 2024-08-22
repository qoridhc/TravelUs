package com.ssafy.soltravel.domain.redis;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("PreferenceRate")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PreferenceRate {

  @Id
  private String id;//"USD(1437.8)"
  private Set<Long> accounts;
}
