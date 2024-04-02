package com.example.gatherplan.appointment.dto;


import com.example.gatherplan.appointment.repository.entity.embedded.ConfirmedDateTime;
import com.example.gatherplan.common.unit.Address;
import lombok.*;


@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetAppointmentInfoRespDto {
    private Address address;
    private ConfirmedDateTime confirmedDateTime;

}
