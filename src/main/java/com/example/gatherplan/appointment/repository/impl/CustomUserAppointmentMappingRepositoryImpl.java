package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.dto.AppointmentWithHostDto;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.CustomUserAppointmentMappingRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.example.gatherplan.appointment.repository.entity.QUser.user;
import static com.example.gatherplan.appointment.repository.entity.QUserAppointmentMapping.userAppointmentMapping;

@Repository
@Slf4j
public class CustomUserAppointmentMappingRepositoryImpl implements CustomUserAppointmentMappingRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomUserAppointmentMappingRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public String findHostName(Long appointmentId) {
        return jpaQueryFactory
                .select(userAppointmentMapping.nickname)
                .from(userAppointmentMapping)
                .where(userAppointmentMapping.appointmentSeq.eq(appointmentId)
                        .and(userAppointmentMapping.userRole.eq(UserRole.HOST)))
                .fetchOne();
    }

    @Override
    public List<AppointmentWithHostDto> findAllAppointmentWithHost(List<Long> appointmentIdList) {
        return new ArrayList<>(jpaQueryFactory
                .select(Projections.constructor(AppointmentWithHostDto.class,
                        user.name,
                        userAppointmentMapping.appointmentSeq))
                .from(userAppointmentMapping)
                .join(user).on(userAppointmentMapping.userSeq.eq(user.id))
                .where(userAppointmentMapping.appointmentSeq.in(appointmentIdList)
                        .and(userAppointmentMapping.userRole.eq(UserRole.HOST)))
                .fetch());
    }
}
