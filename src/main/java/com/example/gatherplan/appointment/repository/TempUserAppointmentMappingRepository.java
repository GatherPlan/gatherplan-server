package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.repository.entity.TempUserAppointmentMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TempUserAppointmentMappingRepository extends JpaRepository<TempUserAppointmentMapping, Long> {
    List<TempUserAppointmentMapping> findAllByAppointmentSeq(Long appointmentId);

}
