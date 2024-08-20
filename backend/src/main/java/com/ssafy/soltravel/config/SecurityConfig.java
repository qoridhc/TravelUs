package com.ssafy.soltravel.config;

import com.ssafy.soltravel.service.user.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserService userService;

  @Value("${server.front.url}")
  private String FRONT_URL;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(Customizer.withDefaults())                 //등록된 빈에서 CORS 커스텀 설정 찾아서 등록
        .csrf(AbstractHttpConfigurer::disable)           //csrf 비활성화
        .authorizeHttpRequests(requests -> requests      //특정 uri만 허용하고 나머지는 인증받아야함
            .requestMatchers("/", "/login", "/join").permitAll()
            .anyRequest().authenticated()
        ).formLogin(form -> form
            .defaultSuccessUrl("/", true)
            .permitAll()
        ).logout(logout -> logout
            .permitAll()
        );

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }
}


// 인증 실패 시 처리할 엔트리 포인트
class FailedAuthenticatoinEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    response.setContentType("application/json"); // 응답 콘텐츠 타입을 JSON으로 설정
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 응답 상태 코드를 401으로 설정
    response.getWriter()
        .write("{\"code\" : \"UA\", \"message\" : \"UnAuthorized.\"}"); // 응답 바디에 JSON 형태로 메시지 작성

  }
}