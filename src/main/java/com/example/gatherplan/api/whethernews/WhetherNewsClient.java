package com.example.gatherplan.api.whethernews;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WhetherNewsClient {
    private final WebClient webClient;

    public WhetherNewsClient(WebClient.Builder webClientBuilder, @Value("${external.api.whethernews.url}") String url) {
        this.webClient = webClientBuilder.baseUrl(url)
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
