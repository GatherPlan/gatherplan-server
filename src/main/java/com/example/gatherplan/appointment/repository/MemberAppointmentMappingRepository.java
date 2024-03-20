package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.repository.entity.MemberAppointmentMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAppointmentMappingRepository extends JpaRepository<MemberAppointmentMapping, Long> {

}
