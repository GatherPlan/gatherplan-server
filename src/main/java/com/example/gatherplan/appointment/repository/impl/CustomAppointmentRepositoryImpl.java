package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.dto.AppointmentSearchListRespDto;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.CustomAppointmentRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.QUserAppointmentMapping;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
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
    public Page<AppointmentSearchRespDto> findAppointmentSearchListRespDtoListByKeywordAndUserSeq(String keyword, Long userId, PageRequest pageable) {

        QUserAppointmentMapping hostMapping = new QUserAppointmentMapping("hostMapping");

        Predicate[] whereConditions = {
                Objects.nonNull(keyword) ? appointment.appointmentName.contains(keyword)
                        .or(hostMapping.nickname.contains(keyword)) : Expressions.TRUE,
                userAppointmentMapping.userSeq.eq(userId),
                hostMapping.userRole.eq(UserRole.HOST)
        };

        JPAQuery<Appointment> countQuery = jpaQueryFactory.selectFrom(appointment)
                .join(userAppointmentMapping).on(appointment.appointmentCode.eq(userAppointmentMapping.appointmentCode))
                .join(hostMapping).on(hostMapping.appointmentCode.eq(appointment.appointmentCode))
                .where(whereConditions);

        List<AppointmentSearchRespDto> result = jpaQueryFactory
                .selectDistinct(Projections.constructor(AppointmentSearchRespDto.class,
                        appointment.appointmentCode,
                        appointment.appointmentName,
                        appointment.appointmentState,
                        appointment.notice,
                        hostMapping.nickname,
                        hostMapping.userSeq.eq(userId)
                ))
                .from(appointment)
                .join(userAppointmentMapping).on(appointment.appointmentCode.eq(userAppointmentMapping.appointmentCode))
                .join(hostMapping).on(hostMapping.appointmentCode.eq(appointment.appointmentCode))
                .where(whereConditions)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(result, pageable, () -> countQuery.fetch().size());
    }
}
