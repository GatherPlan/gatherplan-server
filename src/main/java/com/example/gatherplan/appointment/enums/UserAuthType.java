package com.example.gatherplan.appointment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserAuthType {
    LOCAL("내장 서비스"),
    KAKAO("카카오 연동"),
    NAVER("네이버 연동"),
    GOOGLE("구글 연동");

    private final String description;

    public String getName() {
        return this.name();
    }
}
