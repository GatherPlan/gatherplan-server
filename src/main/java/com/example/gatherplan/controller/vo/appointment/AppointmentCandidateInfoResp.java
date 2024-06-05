package com.example.gatherplan.controller.vo.appointment;

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
@Schema(description = "회원의 약속 확정 후보 날짜 정보 조회 응답 객체")
public class AppointmentCandidateInfoResp {

    @Schema(description = "약속 후보 날짜", example = "[\"2024-03-18\",\"2024-03-20\"]")
    private LocalDate candidateDate;

    @Schema(description = "시작 시간", example = "18:00")
    private LocalTime startTime;

    @Schema(description = "종료 시간", example = "20:00")
    private LocalTime endTime;

    @Schema(description = "참여 가능한 사용자 정보 리스트", example = "[{\"nickname\" : \"이재훈\", \"isAvailable\" : true, \"userAuthType\" : \"LOCAL\", \"userRole\" : \"GUEST\"}]")
    private List<UserParticipationInfo> userParticipationInfoList;
}
