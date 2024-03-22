package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.appointment.repository.entity.Region;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "약속 장소 검색 응답 객체")
public class SearchPlaceResp {

    @Schema(description = "키워드에 맞는 지역 결과 (성수동)", example = "[서울특별시 성동구 성수동1가, 서울특별시 성동구 성수동2가]")
    List<Region> regionList;
}
