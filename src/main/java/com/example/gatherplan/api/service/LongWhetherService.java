package com.example.gatherplan.api.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class LongWhetherService {

    private final WebClient webClient;

    @Value("${external.api.longwhether.key}")
    private String apiKey;

    public LongWhetherService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://apis.data.go.kr/1360000/MidFcstInfoService")
                .build();
    }

    public Mono<String> callExternalAPI() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/getMidLandFcst")
                        .queryParam("serviceKey", apiKey) // 인증키
                        .queryParam("numOfRows", 10) // 페이지 당 결과 수
                        .queryParam("pageNo", 1) // 페이지번호
                        .queryParam("dataType", "JSON")
                        .queryParam("regId", "11B00000") // 지역번호
                        .queryParam("tmFc", "202403201800") // 발표시각
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}