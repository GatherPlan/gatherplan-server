package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.controller.vo.common.PlaceDetail;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchPlaceDetailRespDto {
    List<PlaceDetail> placeDetails;
}
