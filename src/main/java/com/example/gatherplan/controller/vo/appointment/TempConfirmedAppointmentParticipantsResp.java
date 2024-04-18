package com.example.gatherplan.controller.vo.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = " 비회원의 약속 확정 시간에 참여 가능한 사용자 조회 응답 객체")
public class TempConfirmedAppointmentParticipantsResp {

    @Schema(description = "참여 가능한 사용자 조회 리스트", example = "[박승일,박정빈,이재훈]")
    private List<String> nicknameList;

    public static TempConfirmedAppointmentParticipantsResp of(List<String> nicknameList) {
        return TempConfirmedAppointmentParticipantsResp.builder()
                .nicknameList(nicknameList)
                .build();
    }
}
