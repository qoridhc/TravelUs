package com.goofy.tunabank.v1.provider;

import com.goofy.tunabank.v1.domain.Enum.KeyStatus;
import com.goofy.tunabank.v1.domain.Key;
import com.goofy.tunabank.v1.exception.auth.ExpiredApiKeyException;
import com.goofy.tunabank.v1.exception.auth.InvalidApiKeyException;
import com.goofy.tunabank.v1.repository.KeyRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeyProvider {
  private final KeyRepository keyRepository;

  public boolean validateKey(Key key) {

    // 활성 상태 검사
    if(key.getStatus() == KeyStatus.INACTIVE) {
      throw new InvalidApiKeyException(key.getValue());
    }

    // 만료 기간 검사
    if(key.getExpireAt().isBefore(LocalDateTime.now())) {
      throw new ExpiredApiKeyException(key.getValue(), key.getExpireAt());
    }
    return true;
  }
}
