package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.TimeType;
import com.example.gatherplan.appointment.repository.entity.embedded.ConfirmedDateTime;
import com.example.gatherplan.common.unit.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@Schema(description = "임시회원 약속 현황보기 응답 객체")
public class CheckTempAppointmentResp {

    @Schema(description = "약속 이름", example = "점심약속")
    private String appointmentName;

    @Schema(description = "약속 장소", example = "abcdvvcs11a3")
    private Address address;

    @Schema(description = "호스트 이름", example = "박승일")
    private String hostName;

    @Schema(description = "약속 코드", example = "abcdvvcs11a3")
    private String appointmentCode;

    @Schema(description = "약속 상태", example = "UNCONFIRMED")
    private AppointmentState appointmentState;

    @Schema(description = "약속 후보 시간 타입", example = "[오후,저녁]")
    private List<TimeType> candidateTimeTypeList;

    @Schema(description = "약속 후보 날짜", example = "[2024-03-30, 2024-04-01]")
    private List<LocalDate> candidateDateList;

    @Schema(description = "약속 확정 날짜/시간", example = "2024-03-30 12:30")
    private ConfirmedDateTime confirmedDateTime;
}
