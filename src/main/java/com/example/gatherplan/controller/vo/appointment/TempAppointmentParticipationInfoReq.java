package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.common.unit.TempUserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "비회원의 약속 참여 정보 조회 요청 객체")
public class TempAppointmentParticipationInfoReq {

    @Schema(description = "약속 코드", example = "985a61f6f636")
    @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.")
    private String appointmentCode;

    @Schema(description = "비회원 정보", example = "[\"2024-03-18\",\"2024-03-20\"]")
    @Valid
    private TempUserInfo tempUserInfo;

}
