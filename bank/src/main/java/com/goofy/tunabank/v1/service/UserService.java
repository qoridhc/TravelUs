package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.Enum.KeyType;
import com.goofy.tunabank.v1.domain.Enum.Role;
import com.goofy.tunabank.v1.domain.Key;
import com.goofy.tunabank.v1.domain.User;
import com.goofy.tunabank.v1.dto.user.UserJoinRequestDto;
import com.goofy.tunabank.v1.dto.user.UserJoinResponseDto;
import com.goofy.tunabank.v1.exception.user.EmailDuplicatedException;
import com.goofy.tunabank.v1.provider.KeyProvider;
import com.goofy.tunabank.v1.repository.KeyRepository;
import com.goofy.tunabank.v1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final KeyRepository keyRepository;
  private final KeyProvider keyProvider;

  public UserJoinResponseDto createUser(UserJoinRequestDto request) {

    // 1. 이메일 중복 검사
    String email = request.getUserId();
    User user = userRepository.findByEmail(email).orElse(null);

    if (user != null) {
      throw new EmailDuplicatedException(email);
    }

    // 2. UserKey 생성
    String userKey = keyProvider.generateUserKey();

    // 3. 유저 & 키 엔티티 생성
    User createdUser = User.createUser(email, Role.USER);
    Key createdKey = Key.createKey(createdUser, KeyType.USER, userKey);

    // 4. 저장
    userRepository.save(createdUser);
    keyRepository.save(createdKey);

    // 5. 응답
    return UserJoinResponseDto.builder()
        .message("userService")
        .build();
  }
}
