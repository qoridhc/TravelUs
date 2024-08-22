package com.ssafy.soltravel.service.user;


import com.ssafy.soltravel.domain.User;
import com.ssafy.soltravel.dto.user.UserCreateRequestDto;
import com.ssafy.soltravel.dto.user.UserDetailDto;
import com.ssafy.soltravel.dto.user.UserSearchRequestDto;
import com.ssafy.soltravel.dto.user.UserSearchResponseDto;
import com.ssafy.soltravel.dto.user.api.UserCreateRequestBody;
import com.ssafy.soltravel.exception.UserNotFoundException;
import com.ssafy.soltravel.repository.UserRepository;
import com.ssafy.soltravel.util.LogUtil;
import com.ssafy.soltravel.util.PasswordEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final String API_URI = "/member";
  private final UserRepository userRepository;
  private final Map<String, String> apiKeys;
  private final WebClient webClient;


  // 외부 API 요청용 메서드
  private <T> ResponseEntity<Map<String, Object>> request(
      String uri,
      T requestBody, Class<T> bodyClass
  ) {
    return webClient.post()
        .uri(uri)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(requestBody), bodyClass)
        .retrieve()
        // 에러 처리
        .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
            clientResponse.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .flatMap(body -> {
                  String responseMessage = body.get("responseMessage").toString();  // 원하는 메시지 추출
                  return Mono.error(new WebClientResponseException(
                      clientResponse.statusCode().value(),
                      responseMessage,  // 예외 메시지로 설정
                      clientResponse.headers().asHttpHeaders(),
                      responseMessage.getBytes(),  // 메시지를 바이트 배열로 변환
                      StandardCharsets.UTF_8 // 인코딩 지정
                  ));
                })
        )
        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
        })
        .block();
  }


  // 회원가입
  public void createUser(UserCreateRequestDto createDto) {

    // 외부 API 요청용 Body 생성
    UserCreateRequestBody body = UserCreateRequestBody.builder()
        .apiKey(apiKeys.get("API_KEY"))
        .userId(createDto.getEmail())
        .build();

    // 외부 API 요청
    LogUtil.info("request(create) to API", body);
    ResponseEntity<Map<String, Object>> response = request(
        API_URI, body, UserCreateRequestBody.class
    );

    // 결과를 전달받은 매개변수와 함께 저장
    String userKey = response.getBody().get("userKey").toString();
    createDto.setPassword(PasswordEncoder.encrypt(createDto.getEmail(), createDto.getPassword()));
    User user = convertCreateDtoToUserWithUserKey(createDto, userKey);
    userRepository.save(user);
  }

  // 사용자 계정 검색(리스트)
  public List<UserSearchResponseDto> searchAllUser(UserSearchRequestDto searchDto) {
    List<User> list = userRepository.findAll(searchDto).orElseThrow(
        () -> new UserNotFoundException(searchDto)
    );

    return list.stream().map(this::convertUserToSearchResponseDto).collect(Collectors.toList());
  }

  // 사용자 계정 검색(단건, userId)
  public UserSearchResponseDto searchOneUser(Long userId) {
    User user = userRepository.findByUserId(userId).orElseThrow(
        () -> new UserNotFoundException(userId)
    );

    return convertUserToSearchResponseDto(user);
  }


  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//    User user = userRepository.findByEmail(email).orElseThrow(
//        () -> new RuntimeException(String.format("loadUserByUsername Failed: %s", email))
//    );

    User user = userRepository.findByEmail(email).orElse(createTestUserIfNotExists());

    return UserDetailDto.builder()
        .id(user.getUserId())
        .name(user.getName())
        .email(user.getEmail())
        .password(user.getPassword())
        .role(user.getRole())
        .build();
  }

  // 회원가입 요청 DTO를 유저 엔티티로 변환
  private User convertCreateDtoToUserWithUserKey(UserCreateRequestDto dto, String userKey) {
    return User.createUser(
        dto.getName(),
        dto.getPassword(),
        dto.getEmail(),
        dto.getPhone(),
        dto.getAddress(),
        dto.getBirth(),
        userKey
    );
  }

  // 유저 엔티티를 조회 응답 DTO로 변환
  private UserSearchResponseDto convertUserToSearchResponseDto(User user) {
    return UserSearchResponseDto.builder()
        .userId(user.getUserId())
        .name(user.getName())
        .email(user.getEmail())
        .phone(user.getPhone())
        .address(user.getAddress())
        .birth(user.getBirth())
        .registerAt(user.getRegisterAt())
        .isExit(user.getIsExit())
        .build();
  }


  /*
   *  Test용 메서드
   */
  //유저가 존재하지 않으면 임의로 테스트 유저 생성
  private User createTestUserIfNotExists() {
    User user = userRepository.findByEmail("test@email.com").orElse(
        User.createUser(
            "test",
            "pass",
            "test@email.com",
            "010-1111-1111",
            "testAddress",
            LocalDate.now(),
            "test"
        ));
    return user;
  }
}
