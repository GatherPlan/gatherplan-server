package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.CustomAppointmentRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.example.gatherplan.appointment.repository.entity.QAppointment.appointment;
import static com.example.gatherplan.appointment.repository.entity.QTempUser.tempUser;
import static com.example.gatherplan.appointment.repository.entity.QTempUserAppointmentMapping.tempUserAppointmentMapping;
import static com.example.gatherplan.appointment.repository.entity.QUser.user;
import static com.example.gatherplan.appointment.repository.entity.QUserAppointmentMapping.userAppointmentMapping;

@Repository
public class CustomAppointmentRepositoryImpl implements CustomAppointmentRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public CustomAppointmentRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<Appointment> findByAppointmentCodeAndTempUserInfo(String appointmentCode
            , String nickname, String password, UserRole userRole) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(appointment)
                .join(tempUserAppointmentMapping).on(appointment.id.eq(tempUserAppointmentMapping.appointmentSeq))
                .join(tempUser).on(tempUser.id.eq(tempUserAppointmentMapping.tempUserSeq))
                .where(appointment.appointmentCode.eq(appointmentCode)
                        .and(tempUser.nickname.eq(nickname))
                        .and(tempUser.password.eq(password))
                        .and(tempUserAppointmentMapping.userRole.eq(userRole)))
                .fetchOne());
    }

    @Override
    public Optional<Appointment> findByAppointmentCodeAndUserInfo(String appointmentCode, String email, UserRole userRole) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(appointment)
                .join(userAppointmentMapping).on(appointment.id.eq(userAppointmentMapping.appointmentSeq))
                .join(user).on(user.id.eq(userAppointmentMapping.userSeq))
                .where(appointment.appointmentCode.eq(appointmentCode)
                        .and(user.email.eq(email))
                        .and(userAppointmentMapping.userRole.eq(userRole)))
                .fetchOne());
    }
}
