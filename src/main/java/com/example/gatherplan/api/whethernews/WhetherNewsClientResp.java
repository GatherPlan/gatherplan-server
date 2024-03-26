package com.example.gatherplan.api.whethernews;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WhetherNewsClientResp {
    private List<WhetherNewsClientRespDocument> daily;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WhetherNewsClientRespDocument {
        private String wx_text;
        private String mon;
        private String day;
        private String tmin;
        private String tmax;
    }
}
