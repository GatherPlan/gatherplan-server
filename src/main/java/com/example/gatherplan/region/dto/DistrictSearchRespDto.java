package com.example.gatherplan.region.dto;

import com.example.gatherplan.common.enums.LocationType;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DistrictSearchRespDto {
    private String addressName;
    private LocationType locationType;
}
