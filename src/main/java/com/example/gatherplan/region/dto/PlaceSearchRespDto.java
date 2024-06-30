package com.example.gatherplan.region.dto;

import com.example.gatherplan.common.enums.LocationType;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceSearchRespDto {
    private String addressName;
    private String placeUrl;
    private String placeName;
    private LocationType locationType;
}
