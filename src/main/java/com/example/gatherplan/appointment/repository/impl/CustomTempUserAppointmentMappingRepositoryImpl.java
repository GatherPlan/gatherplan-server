package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.dto.AppointmentParticipationInfoRespDto;
import com.example.gatherplan.appointment.dto.TempAppointmentInfoRespDto;
import com.example.gatherplan.appointment.dto.TempAppointmentParticipationInfoRespDto;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.CustomTempUserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
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
import static com.example.gatherplan.appointment.repository.entity.QTempUser.tempUser;
import static com.example.gatherplan.appointment.repository.entity.QTempUserAppointmentMapping.tempUserAppointmentMapping;
import static com.example.gatherplan.appointment.repository.entity.QUser.user;
import static com.example.gatherplan.appointment.repository.entity.QUserAppointmentMapping.userAppointmentMapping;

@Repository
public class CustomTempUserAppointmentMappingRepositoryImpl implements CustomTempUserAppointmentMappingRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public CustomTempUserAppointmentMappingRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<TempAppointmentInfoRespDto> findAppointmentInfo(String nickname, String password, String appointmentCode) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.constructor(TempAppointmentInfoRespDto.class,
                        appointment.appointmentName,
                        tempUser.nickname,
                        appointment.appointmentState,
                        appointment.appointmentCode,
                        appointment.address,
                        appointment.confirmedDateTime))
                .from(tempUserAppointmentMapping)
                .join(appointment).on(
                        tempUserAppointmentMapping.appointmentSeq.eq(appointment.id)
                                .and(appointment.appointmentCode.eq(appointmentCode)))
                .join(tempUser).on(
                        tempUserAppointmentMapping.appointmentSeq.eq(tempUser.id)
                                .and(tempUser.nickname.eq(nickname).and(tempUser.password.eq(password))))
                .where(tempUserAppointmentMapping.userRole.eq(UserRole.HOST))
                .fetchOne());
    }


    @Override
    public Optional<TempAppointmentParticipationInfoRespDto> findAppointmentParticipationInfo(
            String nickname, String password, String appointmentCode) {

        Appointment findAppointment = jpaQueryFactory
                .selectFrom(appointment)
                .where(appointment.appointmentCode.eq(appointmentCode))
                .fetchFirst();

        List<Tuple> tuples = jpaQueryFactory
                .select(tempUser.nickname, tempUserAppointmentMapping.selectedDateTimeList)
                .from(tempUserAppointmentMapping)
                .join(tempUser).on(tempUserAppointmentMapping.tempUserSeq.eq(tempUser.id))
                .join(appointment).on(tempUserAppointmentMapping.appointmentSeq.eq(appointment.id))
                .where(tempUser.nickname.eq(nickname)
                        .and(appointment.appointmentCode.eq(appointmentCode)))
                .fetch();

        List<TempAppointmentParticipationInfoRespDto.UserParticipationInfo> tempUserParticipationInfoList = new ArrayList<>();

        for (Tuple tuple : tuples) {
            String findNickName = tuple.get(tempUser.nickname);
            List<SelectedDateTime> selectedDateTimeList = tuple.get(userAppointmentMapping.selectedDateTimeList);

            TempAppointmentParticipationInfoRespDto.UserParticipationInfo userParticipationInfo =
                    TempAppointmentParticipationInfoRespDto.UserParticipationInfo.builder()
                            .nickname(findNickName)
                            .selectedDateTime(selectedDateTimeList)
                            .build();

            tempUserParticipationInfoList.add(userParticipationInfo);
        }

        List<AppointmentParticipationInfoRespDto.UserParticipationInfo> userParticipationInfoList = new ArrayList<>();

        for (Tuple tuple : tuples) {
            String findNickName = tuple.get(user.nickname);
            List<SelectedDateTime> selectedDateTimeList = tuple.get(userAppointmentMapping.selectedDateTimeList);

            AppointmentParticipationInfoRespDto.UserParticipationInfo userParticipationInfo =
                    AppointmentParticipationInfoRespDto.UserParticipationInfo.builder()
                            .nickname(findNickName)
                            .selectedDateTime(selectedDateTimeList)
                            .build();

            userParticipationInfoList.add(userParticipationInfo);
        }

        return Optional.ofNullable(TempAppointmentParticipationInfoRespDto.builder()
                .candidateDateList(findAppointment.getCandidateDateList())
                .candidateTimeTypeList(findAppointment.getCandidateTimeTypeList())
                .userParticipationInfoList(userParticipationInfoList)
                .tempUserParticipationInfoList(tempUserParticipationInfoList)
                .build());
    }
}
