package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.repository.MemberTempRepository;
import com.example.gatherplan.appointment.repository.entity.MemberTemp;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MemberTempRepositoryImpl implements MemberTempRepository {

    private final EntityManager entityManager;

    @Autowired
    public MemberTempRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;

    }

    @Override
    public void saveTemporaryMember(MemberTemp memberTemp) {
        entityManager.persist(memberTemp);
    }
}
