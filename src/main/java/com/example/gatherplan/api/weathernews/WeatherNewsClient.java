package com.example.gatherplan.api.weathernews;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class WeatherNewsClient {

    private final WebClient webClient;

    @Value("${external.api.weathernews.url}")
    String baseUrl;


    public WeatherNewsClientResp searchWhetherByRegionCode(String regionCode) {

        return webClient.get()
                .uri(UriComponentsBuilder.fromHttpUrl(baseUrl)
                        .path("/mv3/if/daily.fcgi")
                        .queryParam("region", regionCode)
                        .build().toUri())
                .retrieve()
                .bodyToMono(WeatherNewsClientResp.class)
                .block();
    }
}