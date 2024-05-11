package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.repository.CustomUserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.gatherplan.appointment.repository.entity.QUser.user;
import static com.example.gatherplan.appointment.repository.entity.QUserAppointmentMapping.userAppointmentMapping;

@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomUserRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<String> findAllUserNameByAppointmentId(Long appointmentId) {
        return jpaQueryFactory
                .selectDistinct(user.name)
                .from(user)
                .join(userAppointmentMapping).on(user.id.eq(userAppointmentMapping.userSeq)
                        .and(userAppointmentMapping.appointmentSeq.eq(appointmentId)))
                .fetch();
    }

}
