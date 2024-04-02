package com.example.gatherplan.controller.vo.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "약속 정보 조회 요청 객체")
public class GetAppointmentInfoReq {
    @Schema(description = "약속 코드", example = "abcd 1234 efgh 5678")
    private String appointmentCode;
}
