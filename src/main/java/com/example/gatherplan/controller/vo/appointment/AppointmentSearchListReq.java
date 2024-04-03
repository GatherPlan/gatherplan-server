package com.example.gatherplan.controller.vo.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "약속 목록 키워드 조회 요청 객체")
public class AppointmentSearchListReq {

    @Schema(description = "키워드(약속 이름)", example = "맨땅에 헤딩")
    private String keyword;
}
