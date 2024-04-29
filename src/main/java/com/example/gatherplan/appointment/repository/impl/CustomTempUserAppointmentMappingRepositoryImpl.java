package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.dto.AppointmentWithHostDto;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.CustomTempUserAppointmentMappingRepository;
import com.example.gatherplan.common.unit.ParticipationInfo;
import com.example.gatherplan.common.unit.SelectedDateTime;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.gatherplan.appointment.repository.entity.QTempUser.tempUser;
import static com.example.gatherplan.appointment.repository.entity.QTempUserAppointmentMapping.tempUserAppointmentMapping;


@Repository
public class CustomTempUserAppointmentMappingRepositoryImpl implements CustomTempUserAppointmentMappingRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public CustomTempUserAppointmentMappingRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<ParticipationInfo> findAppointmentParticipationInfo(Long appointmentId) {
        List<Tuple> tuples = jpaQueryFactory
                .select(tempUser.id, tempUser.nickname, tempUser.userAuthType, tempUserAppointmentMapping.selectedDateTimeList)
                .from(tempUserAppointmentMapping)
                .join(tempUser).on(tempUserAppointmentMapping.tempUserSeq.eq(tempUser.id))
                .where(tempUserAppointmentMapping.appointmentSeq.eq(appointmentId))
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
    public String findHostName(Long appointmentId) {
        return jpaQueryFactory
                .select(tempUser.nickname)
                .from(tempUserAppointmentMapping)
                .join(tempUser).on(tempUserAppointmentMapping.tempUserSeq.eq(tempUser.id))
                .where(tempUserAppointmentMapping.appointmentSeq.eq(appointmentId)
                        .and(tempUserAppointmentMapping.userRole.eq(UserRole.HOST)))
                .fetchOne();
    }

    @Override
    public List<AppointmentWithHostDto> findAllAppointmentWithHost(List<Long> appointmentIdList) {
        return new ArrayList<>(jpaQueryFactory
                .select(Projections.constructor(AppointmentWithHostDto.class,
                        tempUser.nickname,
                        tempUserAppointmentMapping.appointmentSeq))
                .from(tempUserAppointmentMapping)
                .join(tempUser).on(tempUserAppointmentMapping.tempUserSeq.eq(tempUser.id))
                .where(tempUserAppointmentMapping.appointmentSeq.in(appointmentIdList)
                        .and(tempUserAppointmentMapping.userRole.eq(UserRole.HOST)))
                .fetch());
    }

}
