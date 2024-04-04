package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.dto.AppointmentInfoRespDto;
import com.example.gatherplan.appointment.dto.AppointmentParticipationInfoRespDto;
import com.example.gatherplan.appointment.dto.AppointmentWithHostRespDto;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.CustomUserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.appointment.repository.entity.embedded.SelectedDateTime;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.gatherplan.appointment.repository.entity.QAppointment.appointment;
import static com.example.gatherplan.appointment.repository.entity.QUser.user;
import static com.example.gatherplan.appointment.repository.entity.QUserAppointmentMapping.userAppointmentMapping;

@Repository
public class CustomUserAppointmentMappingRepositoryImpl implements CustomUserAppointmentMappingRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomUserAppointmentMappingRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public boolean existUserMappedToAppointment(String email, String appointmentCode, UserRole userRole) {
        UserAppointmentMapping result = jpaQueryFactory
                .selectFrom(userAppointmentMapping)
                .join(user).on(user.id.eq(userAppointmentMapping.userSeq))
                .join(appointment).on(userAppointmentMapping.appointmentSeq.eq(appointment.id))
                .where(user.email.eq(email)
                        .and(appointment.appointmentCode.eq(appointmentCode))
                        .and(userAppointmentMapping.userRole.eq(userRole)))
                .fetchFirst();

        return ObjectUtils.isNotEmpty(result);
    }

    @Override
    public List<AppointmentWithHostRespDto> findAllAppointmentsWithHostByEmail(String email) {
        return jpaQueryFactory
                .select(Projections.constructor(AppointmentWithHostRespDto.class,
                        user.nickname,
                        appointment.appointmentCode,
                        appointment.appointmentName,
                        appointment.appointmentState))
                .from(userAppointmentMapping)
                .join(appointment).on(
                        userAppointmentMapping.appointmentSeq.eq(appointment.id))
                .join(user).on(
                        userAppointmentMapping.userSeq.eq(user.id)
                                .and(user.email.eq(email)))
                .where(userAppointmentMapping.userRole.eq(UserRole.HOST))
                .fetch();
    }

    @Override
    public List<AppointmentWithHostRespDto> findAllAppointmentsWithHostByEmailAndKeyword(String email, String keyword) {
        return jpaQueryFactory
                .select(Projections.constructor(AppointmentWithHostRespDto.class,
                        user.nickname,
                        appointment.appointmentCode,
                        appointment.appointmentName,
                        appointment.appointmentState))
                .from(userAppointmentMapping)
                .join(appointment).on(userAppointmentMapping.appointmentSeq.eq(appointment.id))
                .join(user).on(
                        userAppointmentMapping.userSeq.eq(user.id)
                                .and(user.email.eq(email)))
                .where(appointment.appointmentName.contains(keyword)
                        .and(userAppointmentMapping.userRole.eq(UserRole.HOST)))
                .fetch();
    }

    @Override
    public Optional<AppointmentInfoRespDto> findAppointmentInfo(String email, String appointmentCode) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.constructor(AppointmentInfoRespDto.class,
                        appointment.address,
                        appointment.confirmedDateTime,
                        appointment.appointmentName,
                        user.nickname,
                        appointment.appointmentState,
                        appointment.appointmentCode))
                .from(userAppointmentMapping)
                .join(appointment).on(
                        userAppointmentMapping.appointmentSeq.eq(appointment.id)
                                .and(appointment.appointmentCode.eq(appointmentCode)))
                .join(user).on(
                        userAppointmentMapping.appointmentSeq.eq(user.id)
                                .and(user.email.eq(email)))
                .where(userAppointmentMapping.userRole.eq(UserRole.HOST))
                .fetchOne());
    }

    @Override
    public Optional<AppointmentParticipationInfoRespDto> findAppointmentParticipationInfo(
            String email, String appointmentCode) {

        Appointment findAppointment = jpaQueryFactory
                .selectFrom(appointment)
                .where(appointment.appointmentCode.eq(appointmentCode))
                .fetchFirst();

        List<Tuple> tuples = jpaQueryFactory
                .select(user.nickname, userAppointmentMapping.selectedDateTimeList)
                .from(userAppointmentMapping)
                .join(user).on(userAppointmentMapping.userSeq.eq(user.id))
                .join(appointment).on(userAppointmentMapping.appointmentSeq.eq(appointment.id))
                .where(user.email.eq(email)
                        .and(appointment.appointmentCode.eq(appointmentCode)))
                .fetch();

        List<AppointmentParticipationInfoRespDto.UserParticipationInfo> userParticipationInfoList = new ArrayList<>();

        for (Tuple tuple : tuples) {
            String nickname = tuple.get(user.nickname);
            List<SelectedDateTime> selectedDateTimeList = tuple.get(userAppointmentMapping.selectedDateTimeList);

            AppointmentParticipationInfoRespDto.UserParticipationInfo userParticipationInfo =
                    AppointmentParticipationInfoRespDto.UserParticipationInfo.builder()
                            .nickname(nickname)
                            .selectedDateTime(selectedDateTimeList)
                            .build();

            userParticipationInfoList.add(userParticipationInfo);
        }

        return Optional.ofNullable(AppointmentParticipationInfoRespDto.builder()
                .candidateDateList(findAppointment.getCandidateDateList())
                .candidateTimeTypeList(findAppointment.getCandidateTimeTypeList())
                .userParticipationInfo(userParticipationInfoList)
                .build());
    }

    @Override
    public void deleteAllByAppointmentId(Long appointmentId) {
        List<Long> deleteIdList = jpaQueryFactory
                .select(userAppointmentMapping.id)
                .from(userAppointmentMapping)
                .where(userAppointmentMapping.appointmentSeq.eq(appointmentId))
                .fetch();

        jpaQueryFactory
                .delete(userAppointmentMapping)
                .where(userAppointmentMapping.id.in(deleteIdList))
                .execute();
    }

}
