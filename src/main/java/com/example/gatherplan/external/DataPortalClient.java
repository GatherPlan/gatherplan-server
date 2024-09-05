package com.example.gatherplan.external;

import com.example.gatherplan.external.vo.FestivalClientResp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class DataPortalClient {

    private final WebClient webClient;

    @Value("${external.api.data-portal.key}")
    String serviceKey;
    @Value("${external.api.data-portal.url}")
    String baseUrl;

    public FestivalClientResp searchFestival(String fromDate) {

        return webClient.get()
                .uri(UriComponentsBuilder.fromHttpUrl(baseUrl)
                        .path("/searchFestival1")
                        .queryParam("MobileOS", "ETC")
                        .queryParam("MobileApp", "WhenWeMeet")
                        .queryParam("_type", "json")
                        .queryParam("arrange", "R")
                        .queryParam("eventStartDate", fromDate)
                        .queryParam("serviceKey", serviceKey)
                        .build().encode().toUri())
                .retrieve()
                .bodyToMono(FestivalClientResp.class)
                .block();
    }
}
