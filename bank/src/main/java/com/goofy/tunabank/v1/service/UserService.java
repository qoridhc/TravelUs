package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.Enum.Role;
import com.goofy.tunabank.v1.domain.User;
import com.goofy.tunabank.v1.dto.user.UserJoinRequestDto;
import com.goofy.tunabank.v1.dto.user.UserJoinResponseDto;
import com.goofy.tunabank.v1.exception.user.EmailDuplicatedException;
import com.goofy.tunabank.v1.provider.KeyProvider;
import com.goofy.tunabank.v1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final KeyProvider keyProvider;

  public UserJoinResponseDto createUser(UserJoinRequestDto request) {

    String email = request.getUserId();

    // 1. 이메일 중복 검사
    User user = userRepository.findByEmail(email).orElse(null);
    if (user != null) {
      throw new EmailDuplicatedException(email);
    }

    // 2. UserKey 생성
    String userKey = keyProvider.generateUserKey();

    // 3. 유저 엔티티 생성

    return UserJoinResponseDto.builder()
        .message("userService")
        .build();
  }
}
