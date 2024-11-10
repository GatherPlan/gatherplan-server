package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.CustomUserAppointmentMappingRepository;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import static com.example.gatherplan.appointment.repository.entity.QUserAppointmentMapping.userAppointmentMapping;
import static com.example.gatherplan.appointment.repository.entity.QAppointment.appointment;

@Repository
public class CustomUserAppointmentMappingRepositoryImpl implements CustomUserAppointmentMappingRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomUserAppointmentMappingRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public void deleteAllByUserSeq(Long userSeq) {
        JPQLQuery<String> unconfirmedAppointmentCodes = JPAExpressions
                .select(appointment.appointmentCode)
                .from(appointment)
                .where(appointment.appointmentState.eq(AppointmentState.UNCONFIRMED));

        jpaQueryFactory
                .delete(userAppointmentMapping)
                .where(userAppointmentMapping.userSeq.eq(userSeq)
                        .and(userAppointmentMapping.userRole.eq(UserRole.GUEST))
                        .and(userAppointmentMapping.appointmentCode.in(unconfirmedAppointmentCodes)))
                .execute();
    }
}
