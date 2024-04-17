package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.common.unit.ConfirmedDateTime;
import com.example.gatherplan.common.unit.ParticipationInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "약속 확정 시간에 참여 가능한 사용자 조회 요청 객체")
public class ConfirmedAppointmentParticipantsReq {

    @NotBlank(message = "약속 코드는 비어 있을 수 없습니다.")
    @Schema(description = "약속 코드", example = "abcd1234efgh")
    private String appointmentCode;

    @Schema(description = "약속 확정 시간 정보", example = "{\"confirmedDate\": \"2024-03-18\", \"confirmedStartTime\": \"09:00\", \"confirmedEndTime\": \"10:00\"}")
    private ConfirmedDateTime confirmedDateTime;

    @Schema(description = "약속 참여 정보", example = "[{\"nickname\": \"박승일\"," +
            " \"selectedDateTimeList\": [{\"selectedDate\": \"2024-03-18\", \"selectedStartTime\": \"11:00\", \"selectedEndTime\": \"12:00\"}]}]")
    private List<@Valid ParticipationInfo> participationInfoList;
}
