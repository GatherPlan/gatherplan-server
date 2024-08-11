package com.example.gatherplan.controller.vo.tempappointment;

import com.example.gatherplan.common.unit.TempUserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "비회원의 지정 약속의 나의 참여 정보 조회 요청")
public class TempAppointmentMyParticipantReq {

    @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.")
    @Schema(description = "약속 코드", example = "985a61f6f636")
    private String appointmentCode;

    @Valid
    @NotNull
    @Schema(description = "비회원 정보", example = "{\"nickname\": \"홍길동\",\"password\": \"abc1234\"}")
    private TempUserInfo tempUserInfo;
}
