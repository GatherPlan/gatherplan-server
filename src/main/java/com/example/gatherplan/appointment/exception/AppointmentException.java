package com.example.gatherplan.appointment.exception;

import com.example.gatherplan.common.exception.BusinessException;
import com.example.gatherplan.common.exception.ErrorCode;

public class AppointmentException extends BusinessException {
    public AppointmentException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public AppointmentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
