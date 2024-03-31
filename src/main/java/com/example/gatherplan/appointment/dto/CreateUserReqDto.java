package com.example.gatherplan.appointment.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateUserReqDto {

    private String email;

    private String authCode;

    private String nickname;

    private String password;
}
