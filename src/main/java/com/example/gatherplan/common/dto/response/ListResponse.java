package com.example.gatherplan.common.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ListResponse<T> {

    private MetaData metaData;
    private T data;

    public ListResponse(MetaData metaData, T data) {
        this.metaData = metaData;
        this.data = data;
    }

    public static <T> ListResponse<T> of(MetaData metaData, T data) {
        return ListResponse.<T>builder()
                .metaData(metaData)
                .data(data)
                .build();
    }

}