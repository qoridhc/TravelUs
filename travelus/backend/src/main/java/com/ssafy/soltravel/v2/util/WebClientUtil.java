package com.ssafy.soltravel.v2.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class WebClientUtil {

  private final WebClient webClient;

  public <T> ResponseEntity<Map<String, Object>> request(
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
                  try {
                    // ObjectMapper를 사용하여 Map을 JSON 문자열로 변환
                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonErrorMessage = objectMapper.writeValueAsString(body);

                    return Mono.error(new WebClientResponseException(
                        clientResponse.statusCode().value(),
                        jsonErrorMessage,
                        clientResponse.headers().asHttpHeaders(),
                        null,
                        StandardCharsets.UTF_8
                    ));
                  } catch (Exception e) {
                    // JSON 변환 중 에러가 발생하면 기본 메시지 사용
                    return Mono.error(new WebClientResponseException(
                        clientResponse.statusCode().value(),
                        "Error processing response",
                        clientResponse.headers().asHttpHeaders(),
                        null,
                        StandardCharsets.UTF_8
                    ));
                  }
                })
        )
        .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
        })
        .block();
  }
}
