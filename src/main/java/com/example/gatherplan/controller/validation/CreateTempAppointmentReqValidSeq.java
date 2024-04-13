package com.example.gatherplan.controller.validation;

import jakarta.validation.GroupSequence;

@GroupSequence({NotBlankAppointmentName.class, SizeCheckAppointmentName.class, NotBlankNickName.class
        , SizeCheckNickName.class, NotBlankPassword.class, SizeCheckPassword.class})
public interface CreateTempAppointmentReqValidSeq {
}
