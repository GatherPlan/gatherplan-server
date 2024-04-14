package com.example.gatherplan.common.unit;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipationInfo {
    private String nickname;
    private List<SelectedDateTime> selectedDateTimeList;
}
