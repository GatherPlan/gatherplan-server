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
    private List<WhetherNewsClientRespDto> daily;
}
