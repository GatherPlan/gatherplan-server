package com.example.gatherplan.common.unit;

import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.appointment.enums.UserRole;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserParticipationInfo {
    private String nickname;
    private boolean isAvailable;
    private UserAuthType userAuthType;
    private UserRole userRole;

    public boolean getIsAvailable(){
        return this.isAvailable;
    }
}
