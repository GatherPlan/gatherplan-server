package com.example.gatherplan.appointment.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ShortWhetherService {

    private final WebClient webClient;

    @Value("${external.api.shortwhether.key}")
    private String apiKey;

    public ShortWhetherService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0")
                .build();
    }

    public Mono<String> callExternalAPI() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/getVilageFcst")
                        .queryParam("serviceKey", apiKey) // 인증키
                        .queryParam("numOfRows", 12) // 페이지 당 결과 수
                        .queryParam("pageNo", 68) // 페이지번호 (1씩 늘어날때마다 1시간씩, 68까지)
                        .queryParam("dataType", "JSON")
                        .queryParam("base_date", "20240321")
                        .queryParam("base_time", "0500")
                        .queryParam("nx", 55)
                        .queryParam("ny", 127)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}