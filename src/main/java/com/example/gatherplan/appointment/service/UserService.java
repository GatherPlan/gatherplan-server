package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.CreateUserReqDto;
import com.example.gatherplan.appointment.dto.UserInfoRespDto;
import com.example.gatherplan.common.config.jwt.UserInfo;

public interface UserService {
    void authenticateEmail(String email);

    void joinUser(CreateUserReqDto createUserReqDto);

    boolean checkHost(String appointmentCode, Long userId);

    boolean checkJoin(String appointmentCode, Long userId);

    boolean checkName(String appointmentCode, String name);

    boolean checkNickname(String appointmentCode, String nickname);

    void authenticateEmailForPasswordReset(String email);

    void resetPassword(String email, String authCode, String password);

    UserInfoRespDto retrieveUserInfo(UserInfo userInfo);

    void deleteUser(UserInfo userInfo);

    void updateUser(String name, Long id);
}
