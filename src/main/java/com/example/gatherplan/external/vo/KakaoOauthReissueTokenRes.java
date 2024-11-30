package com.example.gatherplan.external.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "토큰 재발급 응답")
public class KakaoOauthReissueTokenRes {

    @JsonProperty("access_token")
    private String accessToken;
}
