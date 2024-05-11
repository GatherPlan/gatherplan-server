package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.dto.AppointmentWithHostDto;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.CustomUserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.common.unit.ParticipationInfo;
import com.example.gatherplan.common.unit.SelectedDateTime;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.gatherplan.appointment.repository.entity.QAppointment.appointment;
import static com.example.gatherplan.appointment.repository.entity.QUser.user;
import static com.example.gatherplan.appointment.repository.entity.QUserAppointmentMapping.userAppointmentMapping;

@Repository
@Slf4j
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
                .select(userAppointmentMapping.nickname)
                .from(userAppointmentMapping)
                .where(userAppointmentMapping.appointmentSeq.eq(appointmentId)
                        .and(userAppointmentMapping.userRole.eq(UserRole.HOST)))
                .fetchOne();
    }

    @Override
    public List<ParticipationInfo> findAppointmentParticipationInfo(Long appointmentId) {
        List<Tuple> tuples = jpaQueryFactory
                .select(user.id, user.name, user.userAuthType, userAppointmentMapping.selectedDateTimeList)
                .from(userAppointmentMapping)
                .join(user).on(userAppointmentMapping.userSeq.eq(user.id))
                .where(userAppointmentMapping.appointmentSeq.eq(appointmentId))
                .fetch();

        Map<String, List<SelectedDateTime>> participationMap = tuples.stream()
                .filter(tuple -> tuple.get(0, String.class) != null)
                .collect(Collectors.groupingBy(
                        tuple -> Optional.ofNullable(tuple.get(0, String.class)).orElse("Unknown"),
                        Collectors.mapping(
                                tuple -> tuple.get(1, SelectedDateTime.class),
                                Collectors.toList()
                        )
                ));

        return participationMap.entrySet().stream()
                .map(entry -> ParticipationInfo.builder()
                        .nickname(entry.getKey())
                        .selectedDateTimeList(entry.getValue())
                        .build()).toList();
    }

    @Override
    public List<AppointmentWithHostDto> findAllAppointmentWithHost(List<Long> appointmentIdList) {
        return new ArrayList<>(jpaQueryFactory
                .select(Projections.constructor(AppointmentWithHostDto.class,
                        user.name,
                        userAppointmentMapping.appointmentSeq))
                .from(userAppointmentMapping)
                .join(user).on(userAppointmentMapping.userSeq.eq(user.id))
                .where(userAppointmentMapping.appointmentSeq.in(appointmentIdList)
                        .and(userAppointmentMapping.userRole.eq(UserRole.HOST)))
                .fetch());
    }
}
