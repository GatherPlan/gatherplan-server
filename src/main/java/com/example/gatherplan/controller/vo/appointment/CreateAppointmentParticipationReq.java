package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.common.unit.SelectedDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "회원의 약속 참여하기 요청")
public class CreateAppointmentParticipationReq {

    @NotBlank
    @Schema(description = "약속 코드", example = "abcd1234efgh")
    private String appointmentCode;

    @Valid
    @Schema(description = "선택 날짜 및 시간 정보 리스트",
            example = "[{\"selectedDate\": \"2024-04-10\"," +
                    "\"selectedStartTime\": \"15:00\"," +
                    "\"selectedEndTime\": \"18:00\"}," +
                    "{\"selectedDate\": \"2024-04-10\"," +
                    "\"selectedStartTime\": \"19:00\"," +
                    "\"selectedEndTime\": \"21:00\"}]")
    private List<SelectedDateTime> selectedDateTimeList;
}
