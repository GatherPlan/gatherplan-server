package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAppointmentMappingRepository extends JpaRepository<UserAppointmentMapping, Long> {

}
