package com.example.gatherplan.controller.validation;

import com.example.gatherplan.common.validation.*;
import jakarta.validation.GroupSequence;

@GroupSequence({NotBlankEmail.class, PatternCheckEmail.class, NotBlankAuthcode.class, SizeCheckAuthcode.class
        , NotBlankNickName.class, SizeCheckNickName.class, NotBlankPassword.class, SizeCheckPassword.class,
        PatternCheckPassword.class})
public interface CreateUserReqValidSeq {
}
