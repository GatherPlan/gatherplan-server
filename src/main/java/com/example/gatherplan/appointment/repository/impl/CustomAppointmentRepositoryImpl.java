package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.repository.CustomAppointmentRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.common.unit.CustomPageRequest;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import static com.example.gatherplan.appointment.repository.entity.QAppointment.appointment;
import static com.example.gatherplan.appointment.repository.entity.QUserAppointmentMapping.userAppointmentMapping;

import java.util.List;

@Repository
public class CustomAppointmentRepositoryImpl implements CustomAppointmentRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomAppointmentRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Appointment> findAllByUserIdAndKeywordContaining(Long userId, String keyword, CustomPageRequest customPageRequest) {
        List<Appointment> result = jpaQueryFactory
                .select(appointment)
                .from(userAppointmentMapping)
                .join(appointment).on(userAppointmentMapping.appointmentCode.eq(appointment.appointmentCode))
                .where(userAppointmentMapping.userSeq.eq(userId)
                        .and(keywordContains(keyword)))
                .orderBy(appointment.createdAt.desc().nullsLast())
                .offset(customPageRequest.getOffset())
                .limit(customPageRequest.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(appointment)
                .from(userAppointmentMapping)
                .join(appointment).on(userAppointmentMapping.appointmentCode.eq(appointment.appointmentCode))
                .where(userAppointmentMapping.userSeq.eq(userId)
                        .and(keywordContains(keyword)))
                .fetch()
                .size();

        return new PageImpl<>(result, customPageRequest, total);
    }

    private Predicate keywordContains(String keyword) {
        return StringUtils.isNotBlank(keyword) ? appointment.appointmentName.contains(keyword) : null;
    }

}
