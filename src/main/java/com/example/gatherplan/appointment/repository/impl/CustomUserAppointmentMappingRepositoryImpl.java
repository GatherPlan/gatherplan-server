package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.dto.AppointmentInfoDto;
import com.example.gatherplan.appointment.dto.AppointmentParticipationInfoDto;
import com.example.gatherplan.appointment.dto.UserAppointmentInfoDto;
import com.example.gatherplan.appointment.dto.UserAppointmentKeywordInfoDto;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.CustomUserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.entity.embedded.SelectedDateTime;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
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
    public Boolean existUserMappedToAppointment(String email, String appointmentCode, UserRole userRole) {
        return jpaQueryFactory
                .select(user.id.isNotNull())
                .from(userAppointmentMapping)
                .join(user).on(user.id.eq(userAppointmentMapping.userSeq))
                .join(appointment).on(userAppointmentMapping.appointmentSeq.eq(appointment.id))
                .where(user.email.eq(email)
                        .and(appointment.appointmentCode.eq(appointmentCode))
                        .and(userAppointmentMapping.userRole.eq(userRole)))
                .fetchOne();
    }

    @Override
    public List<UserAppointmentInfoDto> findAllAppointmentsWithHostByEmail(String email) {
        Long userId = jpaQueryFactory
                .select(user.id)
                .from(user)
                .where(user.email.eq(email))
                .fetchOne();

        return jpaQueryFactory
                .select(Projections.constructor(UserAppointmentInfoDto.class,
                        user.nickname,
                        appointment.appointmentCode,
                        appointment.appointmentName,
                        appointment.appointmentState))
                .from(userAppointmentMapping)
                .join(appointment).on(userAppointmentMapping.appointmentSeq.eq(appointment.id))
                .join(user).on(userAppointmentMapping.userSeq.eq(user.id).and(userAppointmentMapping.userRole.eq(UserRole.HOST)))
                .where(userAppointmentMapping.userSeq.eq(userId))
                .fetch();
    }

    @Override
    public List<UserAppointmentKeywordInfoDto> findAllAppointmentsWithHostByEmailAndKeyword(String email, String keyword) {
        Long userId = jpaQueryFactory
                .select(user.id)
                .from(user)
                .where(user.email.eq(email))
                .fetchOne();

        return jpaQueryFactory
                .select(Projections.constructor(UserAppointmentKeywordInfoDto.class,
                        user.nickname,
                        appointment.appointmentCode,
                        appointment.appointmentName,
                        appointment.appointmentState))
                .from(userAppointmentMapping)
                .join(appointment).on(userAppointmentMapping.appointmentSeq.eq(appointment.id))
                .join(user).on(userAppointmentMapping.userSeq.eq(user.id).and(userAppointmentMapping.userRole.eq(UserRole.HOST)))
                .where(userAppointmentMapping.userSeq.eq(userId).and(appointment.appointmentName.contains(keyword)))
                .fetch();
    }

    @Override
    public Optional<AppointmentInfoDto> findAppointmentInfoDto(String email, String appointmentCode) {
        Long userId = jpaQueryFactory
                .select(user.id)
                .from(user)
                .where(user.email.eq(email))
                .fetchOne();

        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.constructor(AppointmentInfoDto.class,
                        appointment.address,
                        appointment.confirmedDateTime))
                .from(userAppointmentMapping)
                .join(appointment).on(userAppointmentMapping.appointmentSeq.eq(appointment.id)
                        .and(appointment.appointmentCode.eq(appointmentCode)))
                .join(user).on(userAppointmentMapping.appointmentSeq.eq(userId))
                .where(userAppointmentMapping.userRole.eq(UserRole.GUEST))
                .fetchOne());
    }

    @Override
    public List<AppointmentParticipationInfoDto.UserParticipationInfo> findAppointmentParticipationInfoList(
            String email, String appointmentCode) {
        List<Tuple> tuples = jpaQueryFactory
                .select(user.nickname, userAppointmentMapping.selectedDateTimeList)
                .from(userAppointmentMapping)
                .join(user).on(userAppointmentMapping.userSeq.eq(user.id))
                .join(appointment).on(userAppointmentMapping.appointmentSeq.eq(appointment.id))
                .where(user.email.eq(email)
                        .and(appointment.appointmentCode.eq(appointmentCode)))
                .fetch();

        List<AppointmentParticipationInfoDto.UserParticipationInfo> userParticipationInfoList = new ArrayList<>();
        for (Tuple tuple : tuples) {
            String nickname = tuple.get(user.nickname);
            List<SelectedDateTime> selectedDateTimeList = tuple.get(userAppointmentMapping.selectedDateTimeList);

            AppointmentParticipationInfoDto.UserParticipationInfo userParticipationInfo =
                    AppointmentParticipationInfoDto.UserParticipationInfo.builder()
                            .nickname(nickname)
                            .selectedDateTime(selectedDateTimeList)
                            .build();

            userParticipationInfoList.add(userParticipationInfo);
        }

        return userParticipationInfoList;
    }

}
