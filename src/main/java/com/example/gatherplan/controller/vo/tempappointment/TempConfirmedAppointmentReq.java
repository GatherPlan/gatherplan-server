package com.example.gatherplan.controller.vo.tempappointment;

import com.example.gatherplan.common.unit.ConfirmedDateTime;
import com.example.gatherplan.common.unit.TempUserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = " 비회원의 약속 확정 요청 객체")
public class TempConfirmedAppointmentReq {

    @NotBlank(message = "약속 코드는 비어 있을 수 없습니다.")
    @Schema(description = "약속 코드", example = "abcd1234efgh")
    private String appointmentCode;

    @Schema(description = "약속 확정 시간 정보", example = "{\"confirmedDate\": \"2024-03-18\", \"confirmedStartTime\": \"09:00\", \"confirmedEndTime\": \"10:00\"}")
    @Valid
    private ConfirmedDateTime confirmedDateTime;

    @Schema(description = "비회원 정보", example = "{\"nickname\": \"홍길동\",\"password\": \"abc1234\"}")
    @Valid
    private TempUserInfo tempUserInfo;

    @Schema(description = "참여 가능 사용자 닉네임 리스트", example = "[이재훈,박정빈,박승일]")
    private List<String> nicknameList;
}
