package com.example.gatherplan.controller.validation;

import com.example.gatherplan.common.validation.NotBlankAppointmentName;
import com.example.gatherplan.common.validation.SizeCheckAppointmentName;
import jakarta.validation.GroupSequence;

@GroupSequence({NotBlankAppointmentName.class, SizeCheckAppointmentName.class})
public interface CreateAppointmentReqValidSeq {
}
