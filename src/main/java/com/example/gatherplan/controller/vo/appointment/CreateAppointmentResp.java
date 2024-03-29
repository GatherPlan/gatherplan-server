package com.example.gatherplan.controller.vo.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Schema(description = "약속 만들기 응답 객체")
public class CreateAppointmentResp {

    @NotNull
    @Schema(description = "약속 코드", example = "abcdvvcs11a3")
    private String appointmentCode;
}
