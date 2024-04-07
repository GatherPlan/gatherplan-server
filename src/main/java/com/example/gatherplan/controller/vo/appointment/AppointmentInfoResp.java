package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.repository.entity.embedded.ConfirmedDateTime;
import com.example.gatherplan.common.unit.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "약속 목록 조회 응답 객체")
public class AppointmentInfoResp {

    @Schema(description = "약속 장소", example = "{\"locationType\": \"DETAIL_ADDRESS\"," +
            "\"fullAddress\": \"서울 성동구 성수동2가 289-30\", " +
            "\"placeName\": \"성수역 2호선 2번출구\", \"placeUrl\": \"http://place.map.kakao.com/7942972\"}")
    private Address address;

    @Schema(description = "약속 확정 시간", example = "{\"confirmedStartTime\": \"09:00\", \"confirmedEndTime\": \"10:00\"," +
            " \"confirmedDate\": \"2024-03-18\"}")
    private ConfirmedDateTime confirmedDateTime;

    @Schema(description = "약속 이름", example = "맨땅에 헤딩")
    private String appointmentName;

    @Schema(description = "호스트 이름", example = "박정빈")
    private String hostName;

    @Schema(description = "약속 상태", example = "UNCONFIRMED")
    private AppointmentState appointmentState;

    @Schema(description = "약속 코드", example = "985a61f6f636")
    @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.")
    private String appointmentCode;
}
