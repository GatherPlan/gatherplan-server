package com.example.gatherplan.feature.join.validation;

import com.example.gatherplan.common.validation.*;
import jakarta.validation.GroupSequence;

@GroupSequence({NotBlankEmail.class, PatternCheckEmail.class, NotBlankName.class, SizeCheckName.class , NotBlankPassword.class, SizeCheckPassword.class, PatternCheckPassword.class})
public interface LocalJoinFormValidationSequence {
}
