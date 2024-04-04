package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.repository.CustomTempUserRepository;
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
        List<Long> deleteIdList = jpaQueryFactory
                .select(tempUserAppointmentMapping.tempUserSeq)
                .from(tempUserAppointmentMapping)
                .where(tempUserAppointmentMapping.appointmentSeq.eq(appointmentId))
                .fetch();

        jpaQueryFactory
                .delete(tempUser)
                .where(tempUser.id.in(deleteIdList))
                .execute();
    }
}
