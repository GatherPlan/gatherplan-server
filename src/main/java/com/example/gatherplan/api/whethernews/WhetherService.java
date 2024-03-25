package com.example.gatherplan.api.whethernews;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WhetherService {
    private final WebClient webClient;

    public WhetherService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.kr-weathernews.com/mv3/if/daily.fcgi")
                .build();
    }

    public Mono<String> callExternalAPI(String regionCode) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("region", regionCode)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
