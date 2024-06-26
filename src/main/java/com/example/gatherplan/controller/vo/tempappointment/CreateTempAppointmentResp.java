package com.example.gatherplan.controller.vo.tempappointment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "비회원의 약속 만들기 응답 객체")
public class CreateTempAppointmentResp {

    @Schema(description = "약속 코드", example = "985a61f6f636")
    @NotBlank
    private String appointmentCode;

    public static CreateTempAppointmentResp of(String appointmentCode) {
        return CreateTempAppointmentResp.builder()
                .appointmentCode(appointmentCode)
                .build();
    }
}
