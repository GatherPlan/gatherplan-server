package com.example.gatherplan.common.unit;

import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class CustomPageRequest extends PageRequest {

    protected CustomPageRequest(int pageNumber, int pageSize, Sort sort) {
        super(pageNumber - 1, pageSize, sort);
    }

    public static CustomPageRequest of(int pageNumber, int pageSize, @NonNull Sort sort) {
        return new CustomPageRequest(pageNumber, pageSize, sort);
    }

    public static CustomPageRequest of(int pageNumber, int pageSize) {
        return of(pageNumber, pageSize, Sort.unsorted());
    }
}
