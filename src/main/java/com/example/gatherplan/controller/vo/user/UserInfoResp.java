package com.example.gatherplan.controller.vo.user;

import com.example.gatherplan.appointment.enums.UserAuthType;
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
@Schema(description = "회원 정보 조회 응답")
public class UserInfoResp {

    @NotBlank
    @Schema(description = "사용자 이름", example = "박정빈")
    private String name;

    @NotBlank
    @Schema(description = "사용자 이메일", example = "abcdef@email.com")
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserAuthType userAuthType;
}
