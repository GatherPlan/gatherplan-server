package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.common.unit.CustomPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface CustomAppointmentRepository {

    Page<Appointment> findAllByUserIdAndKeywordContaining(Long userId, String keyword, CustomPageRequest customPageRequest);
}
