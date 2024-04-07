package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.dto.AppointmentParticipationInfoRespDto;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.CustomUserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.appointment.repository.entity.embedded.SelectedDateTime;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public String findHostName(Long appointmentId) {
        return jpaQueryFactory
                .select(user.nickname)
                .from(userAppointmentMapping)
                .join(user).on(userAppointmentMapping.userSeq.eq(user.id))
                .where(userAppointmentMapping.appointmentSeq.eq(appointmentId)
                        .and(userAppointmentMapping.userRole.eq(UserRole.HOST)))
                .fetchOne();
    }

    @Override
    public List<AppointmentParticipationInfoRespDto.UserParticipationInfo> findAppointmentParticipationInfo(Long appointmentId) {
        List<Tuple> tuples = jpaQueryFactory
                .select(user.nickname, userAppointmentMapping.selectedDateTimeList)
                .from(userAppointmentMapping)
                .join(user).on(userAppointmentMapping.userSeq.eq(user.id))
                .where(userAppointmentMapping.appointmentSeq.eq(appointmentId))
                .fetch();

        return tuples.stream()
                .map(tuple -> {
                    String findNickName = tuple.get(user.nickname);
                    List<SelectedDateTime> selectedDateTimeList = tuple.get(userAppointmentMapping.selectedDateTimeList);

                    return AppointmentParticipationInfoRespDto.UserParticipationInfo.builder()
                            .nickname(findNickName)
                            .selectedDateTime(selectedDateTimeList)
                            .build();
                }).toList();
    }
}
