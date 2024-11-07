package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.UserAuthType;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoRespDto {
    private String name;
    private String email;
    private UserAuthType userAuthType;
}
