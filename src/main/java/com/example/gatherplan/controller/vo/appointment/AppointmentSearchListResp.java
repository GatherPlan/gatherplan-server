package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.appointment.enums.AppointmentState;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "회원의 약속 목록 키워드 조회 응답 객체")
public class AppointmentSearchListResp {

    @Schema(description = "약속 이름", example = "맨땅에 헤딩")
    private String appointmentName;

    @Schema(description = "호스트 이름", example = "박정빈")
    private String hostName;

    @Schema(description = "약속 상태", example = "UNCONFIRMED")
    private AppointmentState appointmentState;

    @Schema(description = "약속 코드", example = "985a61f6f636")
    private String appointmentCode;

    @Schema(description = "호스트 여부", example = "true")
    @JsonProperty("isHost")
    private boolean isHost;

    @Schema(description = "공지사항", example = "점심약속입니다.")
    private String notice;
}
