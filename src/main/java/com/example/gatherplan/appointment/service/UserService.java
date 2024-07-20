package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.CreateUserReqDto;

public interface UserService {
    void authenticateEmail(String email);

    void joinUser(CreateUserReqDto createUserReqDto);

    boolean checkHost(String appointmentCode, Long userId);

    boolean checkJoin(String appointmentCode, Long userId);

    boolean checkName(String appointmentCode, String name);

    boolean checkNickname(String appointmentCode, String nickname);
}
