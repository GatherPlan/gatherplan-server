package com.example.gatherplan.api.kakaolocal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KakaoLocationClient {
    private final WebClient webClient;

    public KakaoLocationClient(WebClient.Builder webClientBuilder, @Value("${external.api.kakao.key}") String apiKey
            , @Value("${external.api.kakao.url}") String url) {
        this.webClient = webClientBuilder.baseUrl(url)
                .defaultHeader(HttpHeaders.AUTHORIZATION, apiKey)
                .build();
    }

    public KakaoLocationClientResp searchLocationByKeyword(String keyword, int page, int size) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("query", keyword)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .bodyToMono(KakaoLocationClientResp.class)
                .block();
    }
}