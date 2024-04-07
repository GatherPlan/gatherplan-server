package com.example.gatherplan.controller.vo.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "임시 회원 약속 만들기 응답 객체")
public class CreateTempAppointmentResp {

    @NotNull
    @Schema(description = "약속 코드", example = "abcdvvcs11a3")
    private String appointmentCode;

    public static CreateTempAppointmentResp of(String appointmentCode) {
        return CreateTempAppointmentResp.builder()
                .appointmentCode(appointmentCode)
                .build();
    }
}
