package com.example.gatherplan.controller.vo.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceDetail {
    private String addressName;
    private String placeName;
    private String placeUrl;
}
