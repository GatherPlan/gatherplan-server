package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.repository.entity.embedded.ConfirmedDateTime;
import com.example.gatherplan.common.unit.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "비회원의 약속 목록 조회 응답 객체")
public class TempAppointmentInfoResp {
    @Schema(description = "약속 이름", example = "맨땅에 헤딩")
    private String appointmentName;

    @Schema(description = "호스트 이름", example = "박정빈")
    private String hostName;

    @Schema(description = "약속 상태", example = "UNCONFIRMED")
    private AppointmentState appointmentState;

    @Schema(description = "약속 코드", example = "abcd 1234 efgh 5678")
    private String appointmentCode;
    
    @Schema(description = "약속 장소", example = "서울 성동구 용답동")
    private Address address;

    @Schema(description = "약속 확정 시간", example = "\"2024-03-18,15:00,16:00\"")
    private ConfirmedDateTime confirmedDateTime;
}
