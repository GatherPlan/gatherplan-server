package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.common.config.jwt.RoleType;
import com.example.gatherplan.controller.vo.common.BooleanResp;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoRespDto {
    private String name;
    private String email;
    private UserAuthType userAuthType;
}
