package com.example.gatherplan.api.service;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class KakaoLocalService {
    private final WebClient webClient;

    public KakaoLocalService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://dapi.kakao.com/v2/local/search/keyword")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK 1f7d606dfa9cdde5d43fb2d3ed525686")
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