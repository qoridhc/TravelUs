package com.goofy.tunabank.v1.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class FailedAuthenticatoinEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException e
  ) throws IOException {

    // HttpServletRequest에서 AuthException을 가져옴
    String exceptionMsg = (String) request.getAttribute("exceptionMsg");
    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    String message = (exceptionMsg != null) ? (exceptionMsg):(e.getMessage());
    String body = String.format(
        "{\"status\": \"UNAUTHORIZED\", \"code\": \"401\", \"message\": \"%s\"}",
        message
    );

    response.getWriter().write(body);
  }
}

