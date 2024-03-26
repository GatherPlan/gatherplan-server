package com.example.gatherplan.api;

import com.example.gatherplan.api.kakaolocal.KakaoLocationClient;
import com.example.gatherplan.api.whethernews.WhetherNewsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public KakaoLocationClient kakaoLocationClient(WebClient.Builder webClientBuilder,
                                                   @Value("${external.api.kakao.key}") String apiKey,
                                                   @Value("${external.api.kakao.url}") String url) {
        return new KakaoLocationClient(webClientBuilder, apiKey, url);
    }

    @Bean
    public WhetherNewsClient whetherNewsClient(WebClient.Builder webClientBuilder,
                                               @Value("${external.api.whethernews.url}") String url) {
        return new WhetherNewsClient(webClientBuilder, url);
    }
}
