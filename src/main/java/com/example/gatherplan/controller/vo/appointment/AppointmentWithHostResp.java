package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.appointment.enums.AppointmentState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "약속 목록 조회 응답 객체")
public class AppointmentWithHostResp {
    @Schema(description = "약속 이름", example = "맨땅에 헤딩")
    private String appointmentName;
    @Schema(description = "호스트 이름", example = "박정빈")
    private String hostName;
    @Schema(description = "약속 상태", example = "UNCONFIRMED")
    private AppointmentState appointmentState;
    @Schema(description = "약속 코드", example = "abcd 1234 efgh 5678")
    private String appointmentCode;
}
