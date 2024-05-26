package com.example.gatherplan.controller.vo.tempappointment;

import com.example.gatherplan.common.unit.UserParticipationInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "약속 확정 전, 후보 날짜 정보 응답 객체")
public class TempAppointmentCandidateDateInfoResp {
    private LocalDate candidateDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<UserParticipationInfo> userParticipationInfoList;
}
