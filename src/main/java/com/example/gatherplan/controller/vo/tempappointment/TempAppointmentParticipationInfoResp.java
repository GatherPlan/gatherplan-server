package com.example.gatherplan.controller.vo.tempappointment;

import com.example.gatherplan.common.unit.ParticipationInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "약속 참여 정보 조회 응답 객체")
public class TempAppointmentParticipationInfoResp {

    @Schema(description = "약속 참여 정보", example = "[{\"nickname\": \"박승일\"," +
            " \"selectedDateTime\": [{\"date\": \"2024-03-18\", \"startTime\": \"09:00\", \"endTime\": \"10:00\"}]}]")
    private List<ParticipationInfo> participationInfoList;
}