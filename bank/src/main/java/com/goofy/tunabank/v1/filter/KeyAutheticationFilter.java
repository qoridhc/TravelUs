package com.goofy.tunabank.v1.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goofy.tunabank.v1.domain.Key;
import com.goofy.tunabank.v1.dto.ApiRequestBody;
import com.goofy.tunabank.v1.exception.auth.ApiKeyNotFoundException;
import com.goofy.tunabank.v1.provider.KeyProvider;
import com.goofy.tunabank.v1.repository.KeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class KeyAutheticationFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final KeyRepository keyRepository;
  private final KeyProvider keyProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException
  {
    // 헤더 파싱하기
    ApiRequestBody.Header header = resolveKey(request);

    // 키 존재 유무 검사
    Key key = keyRepository.findByKeyValue(header.getApikey()).orElseThrow(
        () -> new ApiKeyNotFoundException(header.getApikey())
    );

    // api key 유효성 검사 & 인증 정보 설정
    if(keyProvider.validateKey(key)) {
      Authentication auth = getAuthentication(key.getUser());
      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    // 다음 필터로 넘어가기
    filterChain.doFilter(request, response);
  }


  // 요청(헤더) 파싱하기
  private ApiRequestBody.Header resolveKey(HttpServletRequest request) throws IOException {
    ApiRequestBody apiRequestBody = objectMapper.readValue(request.getInputStream(), ApiRequestBody.class);
    return apiRequestBody.getHeader();
  }

  // 인증 정보 생성하기
  private Authentication getAuthentication(com.goofy.tunabank.v1.domain.User user) {
    String role = String.valueOf(user.getRole());
    String userId = String.valueOf(user.getUserId());
    UserDetails managerDetail = new User(userId, "", Collections.singletonList(new SimpleGrantedAuthority(role)));
    return new UsernamePasswordAuthenticationToken(managerDetail, "", managerDetail.getAuthorities());
  }
}
