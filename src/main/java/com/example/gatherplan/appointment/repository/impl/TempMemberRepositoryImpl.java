package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.repository.TempMemberRepository;
import com.example.gatherplan.appointment.repository.entity.TempMember;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TempMemberRepositoryImpl implements TempMemberRepository {

    private final EntityManager entityManager;

    @Autowired
    public TempMemberRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;

    }

    @Override
    public Long saveTempMember(TempMember tempMember) {
        entityManager.persist(tempMember);
        return tempMember.getId();
    }
}
