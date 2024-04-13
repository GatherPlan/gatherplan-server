package com.example.gatherplan.external;

import com.example.gatherplan.external.vo.KeywordPlaceClientResp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class KakaoLocationClient {

    private final WebClient webClient;
    @Value("${external.api.kakao.key}")
    private String apiKey;
    @Value("${external.api.kakao.url}")
    private String baseUrl;

    public KeywordPlaceClientResp searchLocationByKeyword(String keyword, int page, int size) {

        return webClient
                .get()
                .uri(UriComponentsBuilder.fromHttpUrl(baseUrl)
                        .path("v2/local/search/keyword")
                        .queryParam("query", keyword)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build().toUriString())
                .header(HttpHeaders.AUTHORIZATION, apiKey)
                .retrieve()
                .bodyToMono(KeywordPlaceClientResp.class)
                .block();
    }
}