package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.repository.CustomTempUserAppointmentMappingRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.gatherplan.appointment.repository.entity.QTempUserAppointmentMapping.tempUserAppointmentMapping;

@Repository
public class CustomTempUserAppointmentMappingRepositoryImpl implements CustomTempUserAppointmentMappingRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public CustomTempUserAppointmentMappingRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public void deleteAllByAppointmentId(Long appointmentId) {
        List<Long> deleteIdList = jpaQueryFactory
                .select(tempUserAppointmentMapping.id)
                .from(tempUserAppointmentMapping)
                .where(tempUserAppointmentMapping.appointmentSeq.eq(appointmentId))
                .fetch();

        jpaQueryFactory
                .delete(tempUserAppointmentMapping)
                .where(tempUserAppointmentMapping.id.in(deleteIdList))
                .execute();
    }
}
