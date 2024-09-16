package com.goofy.tunabank.v1.filter;

import com.goofy.tunabank.v1.exception.CustomException;
import com.goofy.tunabank.v1.util.LogUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException
  {
    try {
      filterChain.doFilter(request, response);

    } catch(CustomException e) {
      LogUtil.warn(e.getInfo());
      setErrorResponse(response, e);

    } catch (Exception e) {
      LogUtil.error(e.getMessage());

      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.setContentType("application/json");
      response.getWriter().write(String.format("{\"code\": \"%s\", \"status\": %d, \"message\": \"%s\"}", 500, "IS", "Internal Server Error"));
    }
  }

  private void setErrorResponse(HttpServletResponse response, CustomException e) throws IOException {
    response.setStatus(e.getStatus());
    response.setContentType("application/json");

    String json = String.format(
        "{\"status\": \"%s\", \"code\": \"%s\", \"message\": \"%s\"}",
        e.getStatus(),
        e.getCode(),
        e.getMessage()
    );
    response.getWriter().write(json);
  }
}
