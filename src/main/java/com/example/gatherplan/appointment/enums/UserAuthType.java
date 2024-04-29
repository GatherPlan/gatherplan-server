package com.example.gatherplan.appointment.enums;

import com.example.gatherplan.common.exception.BusinessException;
import com.example.gatherplan.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UserAuthType {
    LOCAL("내장 서비스"),
    KAKAO("카카오 연동"),
    NAVER("네이버 연동"),
    GOOGLE("구글 연동"),
    TEMPORARY("임시 회원");

    private final String description;

    public static UserAuthType byUserAuthType(String userAuthType) {
        return Arrays.stream(values())
                .filter(type -> StringUtils.equals(type.name(), userAuthType))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "해당하는 UserAuthType 유형이 없습니다."));
    }

    public String getName() {
        return this.name();
    }
}
