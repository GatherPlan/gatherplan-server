package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.common.unit.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "회원의 약속 미리보기 조회 응답 객체")
public class AppointmentPreviewResp {

    @Schema(description = "약속 이름", example = "맨땅에 헤딩")
    private String appointmentName;

    @Schema(description = "호스트 이름", example = "박정빈")
    private String hostName;

    @Schema(description = "약속 코드", example = "abcd1234efgh")
    private String appointmentCode;

    @Schema(description = "공지사항", example = "점심약속입니다.")
    private String notice;

    @Schema(description = "약속 장소", example = "{\"locationType\": \"DETAIL_ADDRESS\"," +
            "\"fullAddress\": \"서울 성동구 성수동2가 289-30\", " +
            "\"placeName\": \"성수역 2호선 2번출구\", \"placeUrl\": \"http://place.map.kakao.com/7942972\"}")
    private Address address;

    @Schema(description = "약속 후보 날짜", example = "[\"2024-03-18\",\"2024-03-20\"]")
    private List<LocalDate> candidateDateList;

    @Schema(description = "약속 상태", example = "UNCONFIRMED")
    private AppointmentState appointmentState;

}
