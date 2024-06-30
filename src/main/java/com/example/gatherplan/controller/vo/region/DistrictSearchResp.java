package com.example.gatherplan.controller.vo.region;

import com.example.gatherplan.common.enums.LocationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "약속 장소 행정구역 검색 응답 객체")
public class DistrictSearchResp {
    @Schema(description = "행정구역 주소", example = "서울 성동구 용답동")
    private String address;
    @Schema(description = "주소 타입", example = "DISTRICT")
    private LocationType locationType;
}
