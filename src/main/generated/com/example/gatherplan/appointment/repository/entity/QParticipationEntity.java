package com.example.gatherplan.appointment.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QParticipationEntity is a Querydsl query type for ParticipationEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QParticipationEntity extends EntityPathBase<ParticipationEntity> {

    private static final long serialVersionUID = -1358621067L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QParticipationEntity participationEntity = new QParticipationEntity("participationEntity");

    public final QAppointment appointment;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final ListPath<java.time.LocalDateTime, DateTimePath<java.time.LocalDateTime>> selectedDateTime = this.<java.time.LocalDateTime, DateTimePath<java.time.LocalDateTime>>createList("selectedDateTime", java.time.LocalDateTime.class, DateTimePath.class, PathInits.DIRECT2);

    public final EnumPath<com.example.gatherplan.appointment.enums.UserRole> userRole = createEnum("userRole", com.example.gatherplan.appointment.enums.UserRole.class);

    public QParticipationEntity(String variable) {
        this(ParticipationEntity.class, forVariable(variable), INITS);
    }

    public QParticipationEntity(Path<? extends ParticipationEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QParticipationEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QParticipationEntity(PathMetadata metadata, PathInits inits) {
        this(ParticipationEntity.class, metadata, inits);
    }

    public QParticipationEntity(Class<? extends ParticipationEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.appointment = inits.isInitialized("appointment") ? new QAppointment(forProperty("appointment"), inits.get("appointment")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

