package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.dto.TempAppointmentParticipationInfoRespDto;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.CustomTempUserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.entity.embedded.SelectedDateTime;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.gatherplan.appointment.repository.entity.QTempUser.tempUser;
import static com.example.gatherplan.appointment.repository.entity.QTempUserAppointmentMapping.tempUserAppointmentMapping;
import static com.example.gatherplan.appointment.repository.entity.QUser.user;

@Repository
public class CustomTempUserAppointmentMappingRepositoryImpl implements CustomTempUserAppointmentMappingRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public CustomTempUserAppointmentMappingRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<TempAppointmentParticipationInfoRespDto.UserParticipationInfo> findAppointmentParticipationInfo(Long appointmentId) {
        List<Tuple> tuples = jpaQueryFactory
                .select(tempUser.nickname, tempUserAppointmentMapping.selectedDateTimeList)
                .from(tempUserAppointmentMapping)
                .join(tempUser).on(tempUserAppointmentMapping.tempUserSeq.eq(tempUser.id))
                .where(tempUserAppointmentMapping.appointmentSeq.eq(appointmentId))
                .fetch();

        return tuples.stream()
                .map(tuple -> {
                    String findNickName = tuple.get(user.nickname);
                    List<SelectedDateTime> selectedDateTimeList =
                            tuple.get(tempUserAppointmentMapping.selectedDateTimeList);

                    return TempAppointmentParticipationInfoRespDto.UserParticipationInfo.builder()
                            .nickname(findNickName)
                            .selectedDateTime(selectedDateTimeList)
                            .build();
                }).toList();
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
}
