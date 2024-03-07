package com.example.gatherplan.appointment.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -569882359L;

    public static final QMember member = new QMember("member1");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<ParticipationEntity, QParticipationEntity> participationEntities = this.<ParticipationEntity, QParticipationEntity>createList("participationEntities", ParticipationEntity.class, QParticipationEntity.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final EnumPath<com.example.gatherplan.appointment.enums.UserAuthType> userAuthType = createEnum("userAuthType", com.example.gatherplan.appointment.enums.UserAuthType.class);

    public final EnumPath<com.example.gatherplan.appointment.enums.UserType> userType = createEnum("userType", com.example.gatherplan.appointment.enums.UserType.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

