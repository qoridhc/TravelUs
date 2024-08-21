package com.ssafy.soltravel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private final String BASE_URL = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit";

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl(BASE_URL).build();
    }

}
