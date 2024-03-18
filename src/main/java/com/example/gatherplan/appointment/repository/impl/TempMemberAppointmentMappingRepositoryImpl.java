package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.repository.TempMemberAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.entity.TempMemberAppointmentMapping;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class TempMemberAppointmentMappingRepositoryImpl implements TempMemberAppointmentMappingRepository {

    private final EntityManager entityManager;

    @Autowired
    public TempMemberAppointmentMappingRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveTempMemberAppointmentMapping(TempMemberAppointmentMapping tempMemberAppointmentMapping) {
        entityManager.persist(tempMemberAppointmentMapping);
    }

}
