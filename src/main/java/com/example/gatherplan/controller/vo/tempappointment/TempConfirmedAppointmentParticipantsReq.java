package com.example.gatherplan.controller.vo.tempappointment;

import com.example.gatherplan.common.unit.ConfirmedDateTime;
import com.example.gatherplan.common.unit.TempUserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "비회원의 약속 확정 시간에 참여 가능한 사용자 조회 요청 객체")
public class TempConfirmedAppointmentParticipantsReq {

    @NotBlank(message = "약속 코드는 비어 있을 수 없습니다.")
    @Schema(description = "약속 코드", example = "abcd1234efgh")
    private String appointmentCode;

    @Schema(description = "약속 확정 시간 정보", example = "{\"confirmedDate\": \"2024-03-18\", \"confirmedStartTime\": \"09:00\", \"confirmedEndTime\": \"10:00\"}")
    @Valid
    private ConfirmedDateTime confirmedDateTime;

    @Schema(description = "비회원 정보", example = "{\"nickname\": \"홍길동\",\"password\": \"abc1234\"}")
    @Valid
    @NotNull
    private TempUserInfo tempUserInfo;
}
