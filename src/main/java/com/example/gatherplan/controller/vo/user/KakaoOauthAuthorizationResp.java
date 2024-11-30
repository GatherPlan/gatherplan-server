package com.example.gatherplan.controller.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "인가코드 응답")
public class KakaoOauthAuthorizationResp {

    @NotBlank
    @Schema(description = "인가코드", example = "1a2b3c4d5e6f7g8h9i0jklmnopqrstuvwxyz")
    private String authorizationCode;

    public static KakaoOauthAuthorizationResp of(String authorizationCode) {
        return KakaoOauthAuthorizationResp.builder()
                .authorizationCode(authorizationCode)
                .build();
    }
}
