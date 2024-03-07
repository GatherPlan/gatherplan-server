package com.example.gatherplan.appointment.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAppointment is a Querydsl query type for Appointment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAppointment extends EntityPathBase<Appointment> {

    private static final long serialVersionUID = -1558164848L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAppointment appointment = new QAppointment("appointment");

    public final com.example.gatherplan.appointment.repository.entity.embedded.QAddress address;

    public final EnumPath<com.example.gatherplan.appointment.enums.AppointmentState> appointmentState = createEnum("appointmentState", com.example.gatherplan.appointment.enums.AppointmentState.class);

    public final ListPath<java.time.LocalDate, DatePath<java.time.LocalDate>> candidateDates = this.<java.time.LocalDate, DatePath<java.time.LocalDate>>createList("candidateDates", java.time.LocalDate.class, DatePath.class, PathInits.DIRECT2);

    public final ListPath<java.time.LocalTime, TimePath<java.time.LocalTime>> candidateTimes = this.<java.time.LocalTime, TimePath<java.time.LocalTime>>createList("candidateTimes", java.time.LocalTime.class, TimePath.class, PathInits.DIRECT2);

    public final EnumPath<com.example.gatherplan.appointment.enums.CandidateTimeType> candidateTimeType = createEnum("candidateTimeType", com.example.gatherplan.appointment.enums.CandidateTimeType.class);

    public final DatePath<java.time.LocalDate> confirmedDate = createDate("confirmedDate", java.time.LocalDate.class);

    public final TimePath<java.time.LocalTime> confirmedEndTime = createTime("confirmedEndTime", java.time.LocalTime.class);

    public final TimePath<java.time.LocalTime> confirmedStartTime = createTime("confirmedStartTime", java.time.LocalTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<ParticipationEntity, QParticipationEntity> participationEntities = this.<ParticipationEntity, QParticipationEntity>createList("participationEntities", ParticipationEntity.class, QParticipationEntity.class, PathInits.DIRECT2);

    public QAppointment(String variable) {
        this(Appointment.class, forVariable(variable), INITS);
    }

    public QAppointment(Path<? extends Appointment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAppointment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAppointment(PathMetadata metadata, PathInits inits) {
        this(Appointment.class, metadata, inits);
    }

    public QAppointment(Class<? extends Appointment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new com.example.gatherplan.appointment.repository.entity.embedded.QAddress(forProperty("address")) : null;
    }

}

