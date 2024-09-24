package com.example.gatherplan.common.config.jwt;

import com.example.gatherplan.common.exception.BusinessException;
import com.example.gatherplan.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    USER("ROLE_USER", "사용자"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String role;
    private final String description;

    public static RoleType byRole(String role) {
        return Arrays.stream(values())
                .filter(type -> StringUtils.equals(type.getRole(), role))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "잘못된 사용자 유형입니다."));
    }

    public String getName() {
        return this.name();
    }
}
