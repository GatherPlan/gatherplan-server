package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.dto.AppointmentSearchListRespDto;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.CustomAppointmentRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.gatherplan.appointment.repository.entity.QAppointment.appointment;
import static com.example.gatherplan.appointment.repository.entity.QUserAppointmentMapping.userAppointmentMapping;

@Repository
public class CustomAppointmentRepositoryImpl implements CustomAppointmentRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public CustomAppointmentRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<Appointment> findByAppointmentCodeAndUserSeq(String appointmentCode, Long userId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(appointment)
                .join(userAppointmentMapping).on(appointment.appointmentCode.eq(userAppointmentMapping.appointmentCode))
                .where(appointment.appointmentCode.eq(appointmentCode)
                        .and(userAppointmentMapping.userSeq.eq(userId)))
                .fetchOne());
    }

    @Override
    public Optional<Appointment> findByAppointmentCodeAndTempUserInfoAndUserRole(String appointmentCode
            , String nickname, String password, UserRole userRole) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(appointment)
                .join(userAppointmentMapping).on(appointment.appointmentCode.eq(userAppointmentMapping.appointmentCode))
                .where(appointment.appointmentCode.eq(appointmentCode)
                        .and(userAppointmentMapping.nickname.eq(nickname))
                        .and(userAppointmentMapping.tempPassword.eq(password))
                        .and(userAppointmentMapping.userRole.eq(userRole)))
                .fetchOne());
    }

    @Override
    public Optional<Appointment> findByAppointmentCodeAndTempUserInfo(String appointmentCode, String nickname, String password) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(appointment)
                .join(userAppointmentMapping).on(appointment.appointmentCode.eq(userAppointmentMapping.appointmentCode))
                .where(appointment.appointmentCode.eq(appointmentCode)
                        .and(userAppointmentMapping.nickname.eq(nickname))
                        .and(userAppointmentMapping.tempPassword.eq(password)))
                .fetchOne());
    }



    @Override
    public Optional<Appointment> findByAppointmentCodeAndUserSeqAndUserRole(String appointmentCode, Long userId, UserRole userRole) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(appointment)
                .join(userAppointmentMapping).on(appointment.appointmentCode.eq(userAppointmentMapping.appointmentCode))
                .where(appointment.appointmentCode.eq(appointmentCode)
                        .and(userAppointmentMapping.userSeq.eq(userId))
                        .and(userAppointmentMapping.userRole.eq(userRole)))
                .fetchOne());
    }

    @Override
    public List<AppointmentSearchListRespDto> findAppointmentSearchListRespDtoList(
            List<String> appointmentCodeList, String name, String keyword) {

        return new ArrayList<>(jpaQueryFactory
                .selectDistinct(Projections.constructor(AppointmentSearchListRespDto.class,
                        appointment.appointmentCode,
                        appointment.appointmentName,
                        appointment.appointmentState,
                        appointment.notice,
                        userAppointmentMapping.nickname,
                        userAppointmentMapping.nickname.eq(name)
                ))
                .from(userAppointmentMapping)
                .join(appointment).on(appointment.appointmentCode.eq(userAppointmentMapping.appointmentCode))
                .where(userAppointmentMapping.appointmentCode.in(appointmentCodeList).and(userAppointmentMapping.userRole.eq(UserRole.HOST))
                        .and(keyword != null ? appointment.appointmentName.contains(keyword)
                                .or(userAppointmentMapping.nickname.contains(keyword)) : Expressions.TRUE))
                .fetch());
    }

}
