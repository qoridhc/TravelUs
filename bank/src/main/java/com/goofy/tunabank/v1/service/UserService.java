package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.controller.UserDeleteResponseDto;
import com.goofy.tunabank.v1.domain.Enum.KeyType;
import com.goofy.tunabank.v1.domain.Enum.Role;
import com.goofy.tunabank.v1.domain.Key;
import com.goofy.tunabank.v1.domain.User;
import com.goofy.tunabank.v1.dto.user.UserDeleteRequestDto;
import com.goofy.tunabank.v1.dto.user.UserJoinRequestDto;
import com.goofy.tunabank.v1.dto.user.UserJoinResponseDto;
import com.goofy.tunabank.v1.dto.user.UserSearchRequestDto;
import com.goofy.tunabank.v1.dto.user.UserSearchResponseDto;
import com.goofy.tunabank.v1.exception.auth.UserNotFoundException;
import com.goofy.tunabank.v1.exception.user.UserExitException;
import com.goofy.tunabank.v1.exception.user.EmailDuplicatedException;
import com.goofy.tunabank.v1.exception.user.UserKeyNotFoundException;
import com.goofy.tunabank.v1.mapper.UserMapper;
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

  private final UserMapper userMapper;

  private final static String EXIT_USER_MESSAGE="탈퇴 유저입니다.";

  /*
  * 회원 가입
  * */
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

  /*
  * 유저 조회
  * */
  public UserSearchResponseDto searchUser(UserSearchRequestDto request) {

    // 유저 조회
    User user = userRepository.findByEmail(request.getUserId()).orElseThrow(
        () -> new UserNotFoundException(request.getUserId())
    );

    // 탈퇴 유저 처리
    if(user.getIsExit()) {
      return UserSearchResponseDto.builder()
          .userId(EXIT_USER_MESSAGE)
          .userKey("")
          .build();
    }

    // 유저 키 조회
    Key key = keyRepository.findValidUserKeyByUser(user).orElse(null);

    // 유저 키 만료시, 자동 재발급
    if(key == null) {
      key = keyRepository.findUserKeyByUser(user);
      String userKey = keyProvider.generateUserKey();
      key.updateKey(userKey);
    }

    // 반환
    return userMapper.toUserSearchResponseDto(user, key);
  }

  /*
  * 유저 삭제
  */
  public UserDeleteResponseDto deleteUser(UserDeleteRequestDto request) {

    // userKey로 유저조회
    String keyValue = request.getHeader().getUserKey();
    User user = findUserByUserKey(keyValue);

    // 유저 탈퇴 처리
    user.deactiveUser();
    return new UserDeleteResponseDto();
  }

  /*
  * 비즈니스 로직용 유저 조회 메서드
  */
  public User findUserByUserKey(String keyValue) {

    // key 조회
    Key userKey = keyRepository.findByKeyValueWithUser(keyValue).orElseThrow(
        () -> new UserKeyNotFoundException(keyValue)
    );

    // key 유효성 검사
    keyProvider.validateKey(userKey);

    // 유저 조회
    User user = userKey.getUser();

    // 유저 탈퇴 여부 확인
    if(user.getIsExit()){
      throw new UserExitException(user.getEmail());
    }

    return user;
  }

}
