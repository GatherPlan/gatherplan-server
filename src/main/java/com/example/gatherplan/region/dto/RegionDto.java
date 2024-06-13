package com.example.gatherplan.region.dto;

import com.example.gatherplan.common.enums.LocationType;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegionDto {
    private String address;
    private LocationType locationType;
}
