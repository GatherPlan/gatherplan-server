package com.example.gatherplan.appointment.repository;

import java.util.List;


public interface CustomUserAppointmentMappingRepository {

    List<String> findAllByUserSeq(Long userId);
}
