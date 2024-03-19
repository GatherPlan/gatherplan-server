package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.repository.MemberAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.entity.MemberAppointmentMapping;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MemberAppointmentMappingRepositoryImpl implements MemberAppointmentMappingRepository {

    private final EntityManager entityManager;


    @Autowired
    public MemberAppointmentMappingRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public void save(MemberAppointmentMapping memberAppointmentMapping) {
        entityManager.persist(memberAppointmentMapping);
    }
}
