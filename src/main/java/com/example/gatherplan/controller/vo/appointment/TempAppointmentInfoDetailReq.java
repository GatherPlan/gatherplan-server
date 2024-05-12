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
public class TempAppointmentInfoDetailReq {

    @Valid
    @Schema(description = "비회원 정보", example = "{\"nickname\": \"홍길동\",\"password\": \"abc1234\"}")
    private TempUserInfo tempUserInfo;

    @Schema(description = "약속 코드", example = "abcd1234efgh")
    @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.")
    private String appointmentCode;
}
