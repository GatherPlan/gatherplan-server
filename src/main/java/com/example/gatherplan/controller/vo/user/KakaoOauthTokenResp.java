package com.example.gatherplan.controller.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "Kakao Oauth 토큰 응답")
public class KakaoOauthTokenResp {

    @NotBlank
    @Schema(description = "액세스 토큰", example = "1a2b3c4d5e6f7g8h9i0jklmnopqrstuvwxyz")
    private String accessToken;

    @NotBlank
    @Schema(description = "리프레시 토큰", example = "1a2b3c4d5e6f7g8h9i0jklmnopqrstuvwxyz")
    private String refreshToken;

    public static KakaoOauthTokenResp of(String accessToken, String refreshToken) {
        return KakaoOauthTokenResp.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
