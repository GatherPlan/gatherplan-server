package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.common.unit.SelectedDateTime;
import com.example.gatherplan.controller.validation.NotBlankNickName;
import com.example.gatherplan.controller.validation.SizeCheckNickName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "회원의 약속 참여 요청 객체")
public class CreateAppointmentJoinReq {

    @NotBlank(message = "약속 코드는 비어 있을 수 없습니다.")
    @Schema(description = "약속 코드", example = "abcd1234efgh")
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

    @Schema(description = "닉네임", example = "이재훈")
    @NotBlank(message = "닉네임은 공백이 될 수 없습니다.", groups = NotBlankNickName.class)
    @Size(min = 2, max = 6, message = "닉네임은 2자 이상 6자 이하여야 합니다.", groups = SizeCheckNickName.class)
    private String nickname;
}
