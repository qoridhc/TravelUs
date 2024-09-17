package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.User;
import com.goofy.tunabank.v1.dto.auth.ApiKeyIssueReponseDto;
import com.goofy.tunabank.v1.dto.auth.ApiKeyIssueRequestDto;
import com.goofy.tunabank.v1.exception.auth.UserNotFoundException;
import com.goofy.tunabank.v1.repository.UserRepository;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;

  public ApiKeyIssueReponseDto issueApiKey(ApiKeyIssueRequestDto request) {

    // 2. 이메일 등록 유무 검증
    User user = userRepository.findByEmail(request.getManagerId()).orElseThrow(
        () -> new UserNotFoundException(request)
    );

    // 3. API KEY 발급
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[48];

    random.nextBytes(bytes);
    String apiKey = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

    // 4. 저장 및 응답
    return ApiKeyIssueReponseDto.builder()
        .managerId(request.getManagerId())
        .apiKey(apiKey)
        .createAt(LocalDateTime.now())
        .expireAt(LocalDateTime.now().plusYears(1))
        .build();
  }
}
