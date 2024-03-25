package com.example.gatherplan.api.whethernews;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WhetherNewsClientRespDto {
    private String wx_text;
    private String mon;
    private String day;
    private String tmin;
    private String tmax;
}
