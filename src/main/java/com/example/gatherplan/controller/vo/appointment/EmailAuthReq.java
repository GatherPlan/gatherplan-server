package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.controller.validation.NotBlankEmail;
import com.example.gatherplan.controller.validation.PatternCheckEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Schema(description = "이메일 인증 요청 객체")
public class EmailAuthReq {
    @Schema(description = "이메일", example = "email@example.com")
    @NotBlank(message = "이메일은 공백이 될 수 없습니다.", groups = NotBlankEmail.class)
    @Email(message = "이메일 형식이 맞지 않습니다.", groups = PatternCheckEmail.class)
    private String email;
}