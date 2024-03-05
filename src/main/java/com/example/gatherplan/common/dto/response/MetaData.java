package com.example.gatherplan.common.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MetaData {

    private int totalCount;

    public MetaData(int totalCount){
        this.totalCount = totalCount;
    }
}
