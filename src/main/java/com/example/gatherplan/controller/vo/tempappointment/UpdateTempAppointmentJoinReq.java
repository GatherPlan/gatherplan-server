package com.example.gatherplan.controller.vo.tempappointment;

import com.example.gatherplan.common.unit.SelectedDateTime;
import com.example.gatherplan.common.unit.TempUserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "비회원의 약속 참여 변경 요청")
public class UpdateTempAppointmentJoinReq {

    @Schema(description = "약속 코드", example = "985a61f6f636")
    private String appointmentCode;

    @Valid
    @NotNull
    @Schema(description = "비회원 정보", example = "{\"nickname\": \"홍길동\",\"password\": \"abc1234\"}")
    private TempUserInfo tempUserInfo;

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
