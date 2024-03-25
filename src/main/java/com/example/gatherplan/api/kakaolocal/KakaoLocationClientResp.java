package com.example.gatherplan.api.kakaolocal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KakaoLocationClientResp {
    private List<KakaoLocationClientRespDto> documents;
}