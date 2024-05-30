package com.example.gatherplan.controller.vo.tempappointment;

import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.common.unit.Address;
import com.example.gatherplan.common.unit.ConfirmedDateTime;
import com.example.gatherplan.common.unit.UserParticipationInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "비회원의 약속 정보 응답 객체")
public class TempAppointmentInfoResp {

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

    @Schema(description = "약속 확정 날짜/시간", example = "\"2024-03-18 14:00\"")
    private ConfirmedDateTime confirmedDateTime;

    @Schema(description = "약속 상태", example = "CONFIRMED")
    private AppointmentState appointmentState;

    @Schema(description = "약속 후보 날짜", example = "[\"2024-03-18\",\"2024-03-20\"]")
    private List<LocalDate> candidateDateList;

    @Schema(description = "참여 여부", example = "true")
    private boolean isParticipated;

    @Schema(description = "호스트 여부", example = "true")
    private boolean isHost;

    @Schema(description = "참여 가능한 사용자 정보 리스트", example = "[{\"nickname\" : \"이재훈\", \"isAvailable\" : true, \"userAuthType\" : \"LOCAL\", \"userRole\" : \"GUEST\"}]")
    private List<UserParticipationInfo> userParticipationInfoList;

    public boolean getIsHost(){
        return this.isHost;
    }

    public boolean getIsParticipated(){
        return this.isParticipated;
    }

}
