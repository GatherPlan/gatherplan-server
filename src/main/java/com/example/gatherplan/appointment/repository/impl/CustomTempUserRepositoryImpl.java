package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.CustomTempUserRepository;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.gatherplan.appointment.repository.entity.QTempUser.tempUser;
import static com.example.gatherplan.appointment.repository.entity.QTempUserAppointmentMapping.tempUserAppointmentMapping;

@Repository
public class CustomTempUserRepositoryImpl implements CustomTempUserRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomTempUserRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public void deleteAllByAppointmentId(Long appointmentId) {
        jpaQueryFactory
                .delete(tempUser)
                .where(tempUser.id.eq(
                        JPAExpressions.select(tempUserAppointmentMapping.tempUserSeq)
                                .from(tempUserAppointmentMapping)
                                .where(tempUserAppointmentMapping.appointmentSeq.eq(appointmentId))

                ))
                .execute();
    }

    @Override
    public List<String> findAllTempUserNameByAppointmentId(Long appointmentId) {
        return jpaQueryFactory
                .select(tempUser.nickname)
                .from(tempUser)
                .join(tempUserAppointmentMapping).on(tempUser.id.eq(tempUserAppointmentMapping.tempUserSeq)
                        .and(tempUserAppointmentMapping.appointmentSeq.eq(appointmentId))
                        .and(tempUserAppointmentMapping.userRole.eq(UserRole.GUEST)))
                .fetch();
    }

}
