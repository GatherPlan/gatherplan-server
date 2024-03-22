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
@Schema(description = "약속 장소 행정구역 검색 응답 객체")
public class SearchPlaceResp {
    @Schema(description = "약속 장소 행정구역 검색 결과 리스트", example = "뱃놈")
    List<Region> regionList;
}
