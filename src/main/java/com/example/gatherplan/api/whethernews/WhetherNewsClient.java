package com.example.gatherplan.api.whethernews;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WhetherNewsClient {
    private final WebClient webClient;

    public WhetherNewsClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.kr-weathernews.com/mv3/if/daily.fcgi")
                .build();
    }

    public WhetherNewsClientResp searchWhetherByRegionCode(String regionCode) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("region", regionCode)
                        .build())
                .retrieve()
                .bodyToMono(WhetherNewsClientResp.class)
                .block();
    }
}
