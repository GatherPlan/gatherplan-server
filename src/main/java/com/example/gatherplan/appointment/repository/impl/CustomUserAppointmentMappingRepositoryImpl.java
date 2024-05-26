package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.dto.AppointmentWithHostDto;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.CustomUserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.gatherplan.appointment.repository.entity.QAppointment.appointment;
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
                        userAppointmentMapping.nickname,
                        userAppointmentMapping.appointmentSeq))
                .from(userAppointmentMapping)
                .where(userAppointmentMapping.appointmentSeq.in(appointmentIdList)
                        .and(userAppointmentMapping.userRole.eq(UserRole.HOST)))
                .fetch());
    }

    @Override
    public Optional<UserAppointmentMapping> findByAppointmentCodeAndUserSeqAndUserRole(String appointmentCode, Long userId, UserRole userRole) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(userAppointmentMapping)
                .join(appointment).on(appointment.id.eq(userAppointmentMapping.appointmentSeq))
                .where(appointment.appointmentCode.eq(appointmentCode)
                        .and(userAppointmentMapping.userSeq.eq(userId))
                        .and(userAppointmentMapping.userRole.eq(UserRole.HOST)))
                .fetchOne());
    }

    @Override
    public Optional<UserAppointmentMapping> findByAppointmentCodeAndTempInfoAndUserRole(String appointmentCode, String nickname, String password, UserRole userRole) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(userAppointmentMapping)
                .join(appointment).on(appointment.id.eq(userAppointmentMapping.appointmentSeq))
                .where(appointment.appointmentCode.eq(appointmentCode)
                        .and(userAppointmentMapping.nickname.eq(nickname))
                        .and(userAppointmentMapping.tempPassword.eq(password))
                        .and(userAppointmentMapping.userRole.eq(UserRole.HOST)))
                .fetchOne());
    }

    @Override
    public boolean existsByAppointmentCodeAndUserSeqAndUserRole(String appointmentCode, Long userId, UserRole userRole) {
        return jpaQueryFactory
                .selectFrom(userAppointmentMapping)
                .join(appointment).on(appointment.id.eq(userAppointmentMapping.appointmentSeq))
                .where(appointment.appointmentCode.eq(appointmentCode)
                        .and(userAppointmentMapping.userSeq.eq(userId))
                        .and(userAppointmentMapping.userRole.eq(userRole)))
                .fetchFirst().isAvailable();
    }

    @Override
    public boolean existsByAppointmentCodeAndTempUserInfoAndUserRole(String appointmentCode, String nickname, String password, UserRole userRole) {
        return jpaQueryFactory
                .selectFrom(userAppointmentMapping)
                .join(appointment).on(appointment.id.eq(userAppointmentMapping.appointmentSeq))
                .where(appointment.appointmentCode.eq(appointmentCode)
                        .and(userAppointmentMapping.nickname.eq(nickname))
                        .and(userAppointmentMapping.tempPassword.eq(password))
                        .and(userAppointmentMapping.userRole.eq(userRole)))
                .fetchFirst().isAvailable();
    }
}
