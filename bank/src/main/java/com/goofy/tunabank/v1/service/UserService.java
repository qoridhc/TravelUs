package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.Enum.KeyType;
import com.goofy.tunabank.v1.domain.Enum.Role;
import com.goofy.tunabank.v1.domain.Key;
import com.goofy.tunabank.v1.domain.User;
import com.goofy.tunabank.v1.dto.user.UserJoinRequestDto;
import com.goofy.tunabank.v1.dto.user.UserJoinResponseDto;
import com.goofy.tunabank.v1.dto.user.UserSearchRequestDto;
import com.goofy.tunabank.v1.dto.user.UserSearchResponseDto;
import com.goofy.tunabank.v1.exception.auth.UserNotFoundException;
import com.goofy.tunabank.v1.exception.user.UserKeyExpiredException;
import com.goofy.tunabank.v1.exception.user.EmailDuplicatedException;
import com.goofy.tunabank.v1.mapper.UserMapper;
import com.goofy.tunabank.v1.provider.KeyProvider;
import com.goofy.tunabank.v1.repository.KeyRepository;
import com.goofy.tunabank.v1.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
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

  private final UserMapper userMapper;

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
    return userMapper.toUserJoinResponseDto(createdUser, createdKey);
  }

  public UserSearchResponseDto searchUser(UserSearchRequestDto request) {

    // 유저 조회
    User user = userRepository.findByEmail(request.getUserId()).orElseThrow(
        () -> new UserNotFoundException(request.getUserId())
    );

    // 유저 키 조회
    Key key = user.getValidUserKey().orElseThrow(
        () -> new UserKeyNotFoundException(request.getUserId())
    );

    // 유저 키 만료시, 자동 재발급
    if(key.getExpireAt().isBefore(LocalDateTime.now())) {
      String userKey = keyProvider.generateUserKey();
      key.updateKey(userKey);
    }

    // 반환
    return userMapper.toUserSearchResponseDto(user, key);
  }
}
