package com.example.gatherplan.api.weathernews;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WeatherNewsClient {
    private final WebClient webClient;

    public WeatherNewsClient(WebClient.Builder webClientBuilder, @Value("${external.api.weathernews.url}") String url) {
        this.webClient = webClientBuilder.baseUrl(url)
                .build();
    }


    public WeatherNewsClientResp searchWhetherByRegionCode(String regionCode) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("region", regionCode)
                        .build())
                .retrieve()
                .bodyToMono(WeatherNewsClientResp.class)
                .block();
    }
}
