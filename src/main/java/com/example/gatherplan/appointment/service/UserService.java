package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.CreateUserReqDto;

public interface UserService {
    void authenticateEmail(String email);

    void joinUser(CreateUserReqDto createUserReqDto);

}
