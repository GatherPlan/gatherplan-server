package com.example.gatherplan.controller.vo.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "회원의 약속 만들기 응답 객체")
public class CreateAppointmentResp {

    @Schema(description = "약속 코드", example = "985a61f6f636")
    @NotNull
    private String appointmentCode;

    public static CreateAppointmentResp of(String appointmentCode) {
        return CreateAppointmentResp.builder()
                .appointmentCode(appointmentCode)
                .build();
    }
}
