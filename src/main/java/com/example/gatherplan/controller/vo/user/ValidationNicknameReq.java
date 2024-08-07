package com.example.gatherplan.controller.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "회원의 닉네임 중복 여부 확인 요청 객체")
public class ValidationNicknameReq {

    @Schema(description = "약속 코드", example = "985a61f6f636")
    private String appointmentCode;

    @Comment("회원 닉네임")
    @NotBlank(message = "이름은 공백이 될 수 없습니다.")
    @Size(min = 2, max = 6, message = "이름은 2자 이상 6자 이하여야 합니다.")
    private String nickname;
}
