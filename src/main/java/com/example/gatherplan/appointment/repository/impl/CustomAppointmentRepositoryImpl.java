package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.dto.AppointmentSearchListRespDto;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.CustomAppointmentRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.QUserAppointmentMapping;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

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
    public Optional<Appointment> findByAppointmentCodeAndUserSeqAndUserRoleAndAppointmentState(String appointmentCode, Long userId, UserRole userRole, AppointmentState appointmentState) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(appointment)
                .join(userAppointmentMapping).on(appointment.appointmentCode.eq(userAppointmentMapping.appointmentCode))
                .where(appointment.appointmentCode.eq(appointmentCode)
                        .and(userAppointmentMapping.userSeq.eq(userId))
                        .and(userAppointmentMapping.userRole.eq(userRole))
                        .and(appointment.appointmentState.eq(appointmentState)))
                .fetchOne());
    }

    @Override
    public List<AppointmentSearchListRespDto> findAppointmentSearchListRespDtoListByKeywordAndUserSeq(String keyword, Long userId) {

        QUserAppointmentMapping hostMapping = new QUserAppointmentMapping("hostMapping");

        return jpaQueryFactory
                .selectDistinct(Projections.constructor(AppointmentSearchListRespDto.class,
                        appointment.appointmentCode,
                        appointment.appointmentName,
                        appointment.appointmentState,
                        appointment.notice,
                        hostMapping.nickname,
                        hostMapping.userSeq.eq(userId)
                ))
                .from(appointment)
                .join(userAppointmentMapping).on(appointment.appointmentCode.eq(userAppointmentMapping.appointmentCode)
                        .and(userAppointmentMapping.userSeq.eq(userId)))
                .join(hostMapping).on(hostMapping.appointmentCode.eq(appointment.appointmentCode)
                        .and(hostMapping.userRole.eq(UserRole.HOST)))
                .where((keyword != null ? appointment.appointmentName.contains(keyword)
                                .or(hostMapping.nickname.contains(keyword)) : Expressions.TRUE))
                .fetch();
    }
}
