package com.example.gatherplan.external;

import com.example.gatherplan.external.vo.KakaoOauthLogoutRes;
import com.example.gatherplan.external.vo.KakaoOauthReissueTokenRes;
import com.example.gatherplan.external.vo.KakaoClientOauthTokenResp;
import com.example.gatherplan.external.vo.KakaoOauthUserInfoResp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class KakaoOauthClient {

    @Value("${kakao.oauth.client-id}")
    private String clientId;

    @Value("${kakao.oauth.redirect-uri}")
    private String redirectUri;

    public KakaoClientOauthTokenResp getToken(String authorizationCode) {
        String url = "https://kauth.kakao.com/oauth/token";

        return WebClient.builder()
                .baseUrl(url)
                .build()
                .post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("redirect_uri", redirectUri)
                        .with("code", authorizationCode))
                .retrieve()
                .bodyToMono(KakaoClientOauthTokenResp.class)
                .block();
    }

    public KakaoOauthUserInfoResp getUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        return WebClient.builder()
                .baseUrl(url)
                .build()
                .get()
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoOauthUserInfoResp.class)
                .block();
    }

    public KakaoOauthReissueTokenRes reissueToken(String refreshToken) {
        String url = "https://kauth.kakao.com/oauth/token";

        return WebClient.builder()
                .baseUrl(url)
                .build()
                .post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "refresh_token")
                        .with("client_id", clientId)
                        .with("refresh_token", refreshToken))
                .retrieve()
                .bodyToMono(KakaoOauthReissueTokenRes.class)
                .block();
    }

    public KakaoOauthLogoutRes logout(String accessToken) {
        String url = "https://kapi.kakao.com/v1/user/logout";

        return WebClient.builder()
                .baseUrl(url)
                .build()
                .post()
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoOauthLogoutRes.class)
                .block();
    }
}