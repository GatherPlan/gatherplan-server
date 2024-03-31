package com.example.gatherplan.appointment.exception;

import com.example.gatherplan.common.exception.BusinessException;
import com.example.gatherplan.common.exception.ErrorCode;

public class UserException extends BusinessException {
    public UserException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
