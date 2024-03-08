package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.repository.AppointmentRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class AppointmentRepositoryImpl implements AppointmentRepository {
    private final EntityManager entityManager;


    @Autowired
    public AppointmentRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveAppointment(Appointment appointment) {
        entityManager.persist(appointment);
    }


}
