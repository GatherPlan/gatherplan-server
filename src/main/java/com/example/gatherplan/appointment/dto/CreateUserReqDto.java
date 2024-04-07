package com.example.gatherplan.appointment.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateUserReqDto {

    private String email;

    private String authCode;

    private String nickname;

    private String password;
}
