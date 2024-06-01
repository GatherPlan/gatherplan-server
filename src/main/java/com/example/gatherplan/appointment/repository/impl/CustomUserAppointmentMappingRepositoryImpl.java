package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.repository.CustomUserAppointmentMappingRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.example.gatherplan.appointment.repository.entity.QUserAppointmentMapping.userAppointmentMapping;

@Repository
@Slf4j
public class CustomUserAppointmentMappingRepositoryImpl implements CustomUserAppointmentMappingRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomUserAppointmentMappingRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<String> findAllByUserSeq(Long userId) {
        return new ArrayList<>(jpaQueryFactory
                .selectDistinct(userAppointmentMapping.appointmentCode)
                .from(userAppointmentMapping)
                .where(userAppointmentMapping.userSeq.eq(userId))
                .fetch());
    }
}
