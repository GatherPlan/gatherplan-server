package com.example.gatherplan.controller.validation;

import jakarta.validation.GroupSequence;

@GroupSequence({NotBlankAuthcode.class, NotBlankNickName.class, SizeCheckNickName.class, NotBlankPassword.class, SizeCheckPassword.class,
        PatternCheckPassword.class})
public interface TempAppointmentInfoReqValidSeq {
}
