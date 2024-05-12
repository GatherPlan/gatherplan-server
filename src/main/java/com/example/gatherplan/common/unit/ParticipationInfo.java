package com.example.gatherplan.common.unit;

import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.appointment.enums.UserRole;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipationInfo {
    private String nickname;
    private UserAuthType userAuthType;
    private UserRole userRole;
    private List<SelectedDateTime> selectedDateTimeList;
}
