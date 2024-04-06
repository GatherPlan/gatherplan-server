package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.dto.AppointmentParticipationInfoRespDto;
import com.example.gatherplan.appointment.dto.AppointmentWithHostRespDto;
import com.example.gatherplan.appointment.dto.TempAppointmentParticipationInfoRespDto;
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
import static com.example.gatherplan.appointment.repository.entity.QTempUser.tempUser;
import static com.example.gatherplan.appointment.repository.entity.QTempUserAppointmentMapping.tempUserAppointmentMapping;
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
    public String findHostName(Long appointmentId) {
        String hostNameFromTempUser = jpaQueryFactory
                .select(tempUser.nickname)
                .from(tempUserAppointmentMapping)
                .join(tempUser).on(tempUserAppointmentMapping.tempUserSeq.eq(tempUser.id))
                .where(tempUserAppointmentMapping.appointmentSeq.eq(appointmentId)
                        .and(tempUserAppointmentMapping.userRole.eq(UserRole.HOST)))
                .fetchOne();

        if (hostNameFromTempUser != null) {
            return hostNameFromTempUser;
        }

        return jpaQueryFactory
                .select(user.nickname)
                .from(userAppointmentMapping)
                .join(user).on(userAppointmentMapping.userSeq.eq(user.id))
                .where(userAppointmentMapping.appointmentSeq.eq(appointmentId)
                        .and(userAppointmentMapping.userRole.eq(UserRole.HOST)))
                .fetchOne();
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
    public Optional<AppointmentParticipationInfoRespDto> findAppointmentParticipationInfo(Appointment findAppointment) {
        List<Tuple> tuples = jpaQueryFactory
                .select(user.nickname, userAppointmentMapping.selectedDateTimeList)
                .from(userAppointmentMapping)
                .join(user).on(userAppointmentMapping.userSeq.eq(user.id))
                .where(userAppointmentMapping.appointmentSeq.eq(findAppointment.getId()))
                .fetch();

        List<TempAppointmentParticipationInfoRespDto.UserParticipationInfo> tempUserParticipationInfoList =
                getTempUserParticipationInfoList(tuples);

        List<AppointmentParticipationInfoRespDto.UserParticipationInfo> userParticipationInfoList =
                getUserParticipationInfoList(tuples);

        return Optional.ofNullable(AppointmentParticipationInfoRespDto.builder()
                .candidateDateList(findAppointment.getCandidateDateList())
                .candidateTimeTypeList(findAppointment.getCandidateTimeTypeList())
                .userParticipationInfoList(userParticipationInfoList)
                .tempUserParticipationInfoList(tempUserParticipationInfoList)
                .build());
    }


    private List<AppointmentParticipationInfoRespDto.UserParticipationInfo> getUserParticipationInfoList(List<Tuple> tuples) {
        List<AppointmentParticipationInfoRespDto.UserParticipationInfo> result = new ArrayList<>();

        for (Tuple tuple : tuples) {
            String findNickName = tuple.get(user.nickname);
            List<SelectedDateTime> selectedDateTimeList = tuple.get(userAppointmentMapping.selectedDateTimeList);

            AppointmentParticipationInfoRespDto.UserParticipationInfo info =
                    AppointmentParticipationInfoRespDto.UserParticipationInfo.builder()
                            .nickname(findNickName)
                            .selectedDateTime(selectedDateTimeList)
                            .build();
            result.add(info);
        }
        return result;
    }

    private List<TempAppointmentParticipationInfoRespDto.UserParticipationInfo> getTempUserParticipationInfoList(List<Tuple> tuples) {
        List<TempAppointmentParticipationInfoRespDto.UserParticipationInfo> result = new ArrayList<>();

        for (Tuple tuple : tuples) {
            String findNickName = tuple.get(user.nickname);
            List<SelectedDateTime> selectedDateTimeList = tuple.get(tempUserAppointmentMapping.selectedDateTimeList);

            TempAppointmentParticipationInfoRespDto.UserParticipationInfo info =
                    TempAppointmentParticipationInfoRespDto.UserParticipationInfo.builder()
                            .nickname(findNickName)
                            .selectedDateTime(selectedDateTimeList)
                            .build();
            result.add(info);
        }
        return result;
    }
}
