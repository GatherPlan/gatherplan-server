package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.appointment.enums.AppointmentState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "약속 목록 키워드 조회 응답")
public class AppointmentSearchResp {

    @NotBlank
    @Schema(description = "약속 이름", example = "맨땅에 헤딩")
    private String appointmentName;

    @NotBlank
    @Schema(description = "호스트 이름", example = "박정빈")
    private String hostName;

    @NotNull
    @Schema(description = "약속 상태", example = "UNCONFIRMED")
    private AppointmentState appointmentState;

    @NotBlank
    @Schema(description = "약속 코드", example = "985a61f6f636")
    private String appointmentCode;

    @Schema(description = "호스트 여부", example = "true")
    private boolean isHost;

    @Schema(description = "공지사항", example = "점심약속입니다.")
    private String notice;

    public boolean getIsHost() {
        return this.isHost;
    }
}


