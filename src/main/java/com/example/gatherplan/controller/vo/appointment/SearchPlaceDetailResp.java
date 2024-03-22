package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.controller.vo.common.PlaceDetail;
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
@Schema(description = "약속 장소 상세주소 검색 응답 객체")
public class SearchPlaceDetailResp {
    @Schema(description = "약속 장소 상세주소 검색 결과 리스트", example = "뱃놈")
    List<PlaceDetail> placeDetails;
}
