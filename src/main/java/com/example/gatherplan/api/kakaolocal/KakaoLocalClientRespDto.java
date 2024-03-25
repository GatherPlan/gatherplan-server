package com.example.gatherplan.api.kakaolocal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KakaoLocalClientRespDto {
    private String place_name;
    private String address_name;
    private String place_url;
}
