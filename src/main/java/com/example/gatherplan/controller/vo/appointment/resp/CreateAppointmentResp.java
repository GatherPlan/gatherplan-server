package com.example.gatherplan.controller.vo.appointment.resp;

import com.example.gatherplan.appointment.enums.UserType;
import com.example.gatherplan.appointment.repository.entity.embedded.Address;
import com.example.gatherplan.appointment.repository.entity.embedded.CandidateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "회원의 약속 만들기 응답 객체")
@Getter
@AllArgsConstructor
public class CreateAppointmentResp {

    @Schema(description = "약속 이름")
    private String appointmentName;

    @Schema(description = "약속 장소")
    private Address address;

    @Schema(description = "약속 메모")
    private String notice;

    @Schema(description = "호스트 이름")
    private String hostName;

    @Schema(description = "회원 유형")
    private UserType userType;

    @Schema(description = "약속 후보 시간")
    private List<CandidateTime> candidateTimeList;

    @Schema(description = "약속 후보 날짜")
    private List<LocalDate> candidateDateList;
}
