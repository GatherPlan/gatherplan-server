package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.appointment.enums.TimeType;
import com.example.gatherplan.appointment.repository.entity.embedded.SelectedDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@Schema(description = "약속 참여 정보 조회 응답 객체")
public class GetAppointmentParticipationInfoResp {

    @Schema(description = "약속 참여 정보 (회원)", example = "맨땅에 헤딩")
    private List<UserParticipationInfo> userParticipationInfo;
    @Schema(description = "약속 후보 시간 타입 리스트", example = "[MORNING,AFTERNOON]")
    private List<TimeType> candidateTimeTypeList;
    @Schema(description = "약속 후보 날짜 리스트", example = "[2024-03-12, 2024-03-13")
    private List<LocalDate> candidateDateList;

    @Getter
    @Builder
    @Schema(description = "회원 참여 정보")
    public static class UserParticipationInfo {
        @Schema(description = "회원 이름", example = "박승일")
        private String nickname;
        @Schema(description = "회원이 선택한 약속 참여 시간", example = "[{2024-03-12, 15:00, 16:00},{2024-03-12, 17:00, 18:00}]")
        private List<SelectedDateTime> selectedDateTime;
    }

}
