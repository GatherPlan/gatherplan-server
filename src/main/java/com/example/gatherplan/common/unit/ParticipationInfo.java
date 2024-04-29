package com.example.gatherplan.common.unit;

import com.example.gatherplan.appointment.enums.UserAuthType;
import jakarta.validation.Valid;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipationInfo {
    private Long userId;
    private String nickname;
    private UserAuthType userAuthType;
    private List<@Valid SelectedDateTime> selectedDateTimeList;
}
