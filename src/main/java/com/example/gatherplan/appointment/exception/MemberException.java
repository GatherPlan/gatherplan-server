package com.example.gatherplan.appointment.exception;

import com.example.gatherplan.common.exception.BusinessException;
import com.example.gatherplan.common.exception.ErrorCode;

public class MemberException extends BusinessException {
    public MemberException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
