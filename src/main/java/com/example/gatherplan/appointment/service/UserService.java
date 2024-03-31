package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.AuthenticateEmailReqDto;
import com.example.gatherplan.appointment.dto.CreateUserReqDto;

public interface UserService {
    void authenticateEmail(AuthenticateEmailReqDto authenticateEmailReqDto);

    void joinuser(CreateUserReqDto createUserReqDto);

}
