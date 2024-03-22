package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.repository.entity.Region;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchPlaceRespDto {
    List<Region> regionList;
}
