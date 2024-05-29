package com.example.gatherplan.controller.vo.appointment;


import com.example.gatherplan.common.unit.SelectedDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "회원 약속 참여 변경 요청 객체")
public class UpdateAppointmentJoinReq {

    @Schema(description = "약속 코드", example = "985a61f6f636")
    private String appointmentCode;

    @NotEmpty(message = "날짜 및 시간 정보는 비어 있을 수 없습니다.")
    @Schema(description = "선택 날짜 및 시간 정보 리스트",
            example = "[{\"selectedDate\": \"2024-04-10\"," +
                    "\"selectedStartTime\": \"15:00\"," +
                    "\"selectedEndTime\": \"18:00\"}," +
                    "{\"selectedDate\": \"2024-04-10\"," +
                    "\"selectedStartTime\": \"19:00\"," +
                    "\"selectedEndTime\": \"21:00\"}]")
    private List<@Valid SelectedDateTime> selectedDateTimeList;
}
