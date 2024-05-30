package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.dto.AppointmentWithHostDto;
import java.util.List;


public interface CustomUserAppointmentMappingRepository {

    List<AppointmentWithHostDto> findAllAppointmentWithHost(List<String> appointmentCodeList);
}
