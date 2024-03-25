package com.example.gatherplan.appointment.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchWhetherRespDto {
    private String mon;
    private String day;
    private String whetherState;
    private String minTemporary;
    private String maxTemporary;
}
