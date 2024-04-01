package com.example.gatherplan.controller.validation;

import jakarta.validation.GroupSequence;

@GroupSequence({NotBlankAppointmentName.class, SizeCheckAppointmentName.class})
public interface CreateAppointmentReqValidSeq {
}
