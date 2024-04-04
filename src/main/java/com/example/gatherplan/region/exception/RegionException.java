package com.example.gatherplan.region.exception;

import com.example.gatherplan.common.exception.BusinessException;
import com.example.gatherplan.common.exception.ErrorCode;

public class RegionException extends BusinessException {
    public RegionException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public RegionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
