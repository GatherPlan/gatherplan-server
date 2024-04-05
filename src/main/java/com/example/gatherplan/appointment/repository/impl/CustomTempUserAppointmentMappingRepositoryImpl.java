package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.dto.TempAppointmentInfoRespDto;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.CustomTempUserAppointmentMappingRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.example.gatherplan.appointment.repository.entity.QAppointment.appointment;
import static com.example.gatherplan.appointment.repository.entity.QTempUser.tempUser;
import static com.example.gatherplan.appointment.repository.entity.QTempUserAppointmentMapping.tempUserAppointmentMapping;

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
}
