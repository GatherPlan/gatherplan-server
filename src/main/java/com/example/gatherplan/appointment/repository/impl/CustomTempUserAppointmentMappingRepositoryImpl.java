package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.dto.AppointmentParticipationInfoRespDto;
import com.example.gatherplan.appointment.dto.TempAppointmentParticipationInfoRespDto;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.CustomTempUserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.embedded.SelectedDateTime;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Optional<TempAppointmentParticipationInfoRespDto> findAppointmentParticipationInfo(Appointment findAppointment) {
        List<Tuple> tuples = jpaQueryFactory
                .select(tempUser.nickname, tempUserAppointmentMapping.selectedDateTimeList)
                .from(tempUserAppointmentMapping)
                .join(tempUser).on(tempUserAppointmentMapping.tempUserSeq.eq(tempUser.id))
                .where(tempUserAppointmentMapping.appointmentSeq.eq(findAppointment.getId()))
                .fetch();

        List<TempAppointmentParticipationInfoRespDto.UserParticipationInfo> tempUserParticipationInfoList =
                getTempUserParticipationInfoList(tuples);

        List<AppointmentParticipationInfoRespDto.UserParticipationInfo> userParticipationInfoList =
                getUserParticipationInfoList(tuples);

        return Optional.ofNullable(TempAppointmentParticipationInfoRespDto.builder()
                .candidateDateList(findAppointment.getCandidateDateList())
                .candidateTimeTypeList(findAppointment.getCandidateTimeTypeList())
                .userParticipationInfoList(userParticipationInfoList)
                .tempUserParticipationInfoList(tempUserParticipationInfoList)
                .build());
    }

    @Override
    public String findHostName(Long appointmentId) {
        return jpaQueryFactory
                .select(tempUser.nickname)
                .from(tempUserAppointmentMapping)
                .join(tempUser).on(tempUserAppointmentMapping.tempUserSeq.eq(tempUser.id))
                .where(tempUserAppointmentMapping.appointmentSeq.eq(appointmentId)
                        .and(tempUserAppointmentMapping.userRole.eq(UserRole.HOST)))
                .fetchOne();
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
