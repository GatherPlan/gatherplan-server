package com.example.gatherplan.external.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "Kakao Oauth 로그아웃 응답")
public class KakaoOauthLogoutRes {

    private Long id;
}
