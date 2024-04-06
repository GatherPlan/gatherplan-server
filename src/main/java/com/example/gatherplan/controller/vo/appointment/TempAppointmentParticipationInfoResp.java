package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.appointment.dto.TempAppointmentParticipationInfoRespDto;
import com.example.gatherplan.appointment.enums.TimeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@Schema(description = "약속 참여 정보 조회 응답 객체")
public class TempAppointmentParticipationInfoResp {

    @Schema(description = "약속 참여 정보 (회원)", example = "맨땅에 헤딩")
    private List<TempAppointmentParticipationInfoRespDto.UserParticipationInfo> tempUserParticipationInfoList;

    @Schema(description = "약속 참여 정보 (비회원)", example = "맨땅에 헤딩")
    private List<AppointmentParticipationInfoResp.UserParticipationInfo> userParticipationInfoList;

    @Schema(description = "약속 후보 시간" +
            "<br> MORNING : 오전, AFTERNOON : 오후, EVENING : 저녁", example = "[\"MORNING\", \"EVENING\"]")
    private List<TimeType> candidateTimeTypeList;
    @Schema(description = "약속 후보 날짜", example = "[\"2024-03-18\",\"2024-03-20\"]")
    private List<LocalDate> candidateDateList;

}