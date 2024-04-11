package com.example.gatherplan.appointment.exception;

import com.example.gatherplan.common.exception.BusinessException;
import com.example.gatherplan.common.exception.ErrorCode;

public class TempUserException extends BusinessException {
    public TempUserException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public TempUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
