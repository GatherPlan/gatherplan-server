package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.repository.entity.TempUserAppointmentMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempUserAppointmentMappingRepository extends JpaRepository<TempUserAppointmentMapping, Long> {
    void deleteAllByAppointmentSeq(Long appointmentId);

}
