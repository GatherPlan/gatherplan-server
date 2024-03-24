package com.example.gatherplan.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class KakaoLocalService {
    private final WebClient webClient;

    public KakaoLocalService(WebClient.Builder webClientBuilder, @Value("${external.api.kakao.key}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl("https://dapi.kakao.com/v2/local/search/keyword")
                .defaultHeader(HttpHeaders.AUTHORIZATION, apiKey)
                .build();
    }

    public Mono<String> callExternalAPI(String keyword) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("query", keyword)
                        .queryParam("page", 1)
                        .queryParam("size", 15)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}