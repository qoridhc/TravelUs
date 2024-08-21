package com.ssafy.soltravel.service.user;


import com.ssafy.soltravel.domain.User;
import com.ssafy.soltravel.dto.user.UserDetailDto;
import com.ssafy.soltravel.dto.user.UserLoginRequestDto;
import com.ssafy.soltravel.dto.user.UserLoginResponseDto;
import com.ssafy.soltravel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    //로그인
    public UserLoginResponseDto loginUser(UserLoginRequestDto request) {
        log.info("userLogin(UserController): 소셜 로그인 요청");
        log.info("email: {}", request.getEmail());
        return UserLoginResponseDto.builder()
            .accessToken("testAccessToken")
            .refreshToken("testRefreshToken")
            .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//    User user = userRepository.findByEmail(email).orElseThrow(
//        () -> new RuntimeException("load")
//    );

        User user = userRepository.findByEmail(email).orElse(createTestUserIfNotExists());

        return UserDetailDto.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .password(user.getPassword())
            .role(user.getRole())
            .build();
    }

    private User createTestUserIfNotExists() {
        User user = userRepository.findByEmail("test@email.com").orElse(
            User.createUser(
                "test",
                "pass",
                "test@email.com",
                "010-1111-1111",
                "testAddress"
            ));
        return user;
    }
}
