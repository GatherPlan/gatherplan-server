package com.example.gatherplan.region.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalRespDto {
    private String title;

    private String addressName;

    private String placeName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String imagePath;

    private String tel;
}
