package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.common.unit.ConfirmedDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "약속 확정 요청")
public class ConfirmAppointmentReq {

    @NotBlank(message = "약속 코드는 비어 있을 수 없습니다.")
    @Schema(description = "약속 코드", example = "abcd1234efgh")
    private String appointmentCode;

    @NotNull
    @Schema(description = "약속 확정 시간 정보", example = "{\"confirmedDate\": \"2024-03-18\", \"confirmedStartTime\": \"09:00\", \"confirmedEndTime\": \"10:00\"}")
    private ConfirmedDateTime confirmedDateTime;
}
