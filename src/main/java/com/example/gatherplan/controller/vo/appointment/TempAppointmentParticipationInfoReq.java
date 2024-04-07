package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.controller.validation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "비회원의 약속 참여 정보 조회 요청 객체")
public class TempAppointmentParticipationInfoReq {

    @Schema(description = "약속 코드", example = "abcd 1234 efgh 5678")
    @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.")
    private String appointmentCode;

    @Schema(description = "약속 후보 날짜", example = "{\"nickname\": \"홍길동\",\"password\": \"abc1234\"}")
    @Valid
    private TempAppointmentParticipationInfoReq.TempUserInfo tempUserInfo;

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(description = "임시 회원 정보")
    public static class TempUserInfo {
        @Schema(description = "임시 회원 이름", example = "홍길동")
        @NotBlank(message = "이름은 공백이 될 수 없습니다.", groups = NotBlankNickName.class)
        @Size(min = 2, max = 6, message = "이름은 2자 이상 6자 이하여야 합니다.", groups = SizeCheckNickName.class)
        private String nickname;

        @Schema(description = "임시 회원 비밀번호", example = "abc1234")
        @NotBlank(message = "비밀번호는 공백이 될 수 없습니다.", groups = NotBlankPassword.class)
        @Size(min = 4, max = 12, message = "비밀번호는 4자 이상 12자 이하여야 합니다.", groups = SizeCheckPassword.class)
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "비밀번호는 영문자, 숫자를 적어도 하나씩 포함해야 합니다.",
                groups = PatternCheckPassword.class)
        private String password;
    }
}
