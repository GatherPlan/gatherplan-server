package com.example.gatherplan.controller.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "Kakao Oauth 로그아웃 요청")
public class KakaoOauthLoginReq {

    @NotBlank(message = "액세스 토큰은 공백이 될 수 없습니다.")
    @Schema(description = "액세스 토큰", example = "1a2b3c4d5e6f7g8h9i0jklmnopqrstuvwxyz")
    private String accessToken;
}
