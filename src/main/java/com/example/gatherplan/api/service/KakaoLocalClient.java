package com.example.gatherplan.api.service;

import com.example.gatherplan.appointment.dto.SearchPlaceRespDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class KakaoLocalClient {
    private final WebClient webClient;

    public KakaoLocalClient(WebClient.Builder webClientBuilder, @Value("${external.api.kakao.key}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl("https://dapi.kakao.com/v2/local/search/keyword")
                .defaultHeader(HttpHeaders.AUTHORIZATION, apiKey)
                .build();
    }

    public List<SearchPlaceRespDto> callExternalAPI(String keyword) {
        String result = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("")
                        .queryParam("query", keyword)
                        .queryParam("page", 1)
                        .queryParam("size", 15)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<SearchPlaceRespDto> placeDetails = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(result);
            JsonNode documentsNode = jsonNode.get("documents");

            for (JsonNode documentNode : documentsNode) {
                String address = documentNode.get("address_name").asText();
                String placeName = documentNode.get("place_name").asText();
                String url = documentNode.get("place_url").asText();

                SearchPlaceRespDto placeDto = SearchPlaceRespDto.builder()
                        .address(address)
                        .placeName(placeName)
                        .url(url)
                        .build();
                placeDetails.add(placeDto);
            }
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
        }
        return placeDetails;
    }
}