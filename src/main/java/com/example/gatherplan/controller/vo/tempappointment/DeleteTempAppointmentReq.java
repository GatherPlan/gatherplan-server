package com.example.gatherplan.controller.vo.tempappointment;

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
@Schema(description = "비회원의 약속 삭제 요청")
public class DeleteTempAppointmentReq {

    @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.")
    @Schema(description = "약속 코드", example = "985a61f6f636")
    private String appointmentCode;

    @Valid
    @NotNull
    @Schema(description = "약속 후보 날짜", example = "{\"nickname\": \"홍길동\",\"password\": \"abc1234\"}")
    private TempUserInfo tempUserInfo;
}
