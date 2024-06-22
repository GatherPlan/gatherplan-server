package com.example.gatherplan.controller.vo.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "이메일 인증 요청 객체")
public class EmailAuthReq {

    @Schema(description = "이메일", example = "email@example.com")
    @NotBlank(message = "이메일은 공백이 될 수 없습니다.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;
}
