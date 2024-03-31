package com.example.gatherplan.appointment.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * Quser is a Querydsl query type for user
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class Quser extends EntityPathBase<user> {

    private static final long serialVersionUID = -569882359L;

    public static final Quser user = new Quser("user1");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<ParticipationEntity, QParticipationEntity> participationEntities = this.<ParticipationEntity, QParticipationEntity>createList("participationEntities", ParticipationEntity.class, QParticipationEntity.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final EnumPath<com.example.gatherplan.appointment.enums.UserAuthType> userAuthType = createEnum("userAuthType", com.example.gatherplan.appointment.enums.UserAuthType.class);

    public final EnumPath<com.example.gatherplan.appointment.enums.UserType> userType = createEnum("userType", com.example.gatherplan.appointment.enums.UserType.class);

    public Quser(String variable) {
        super(user.class, forVariable(variable));
    }

    public Quser(Path<? extends user> path) {
        super(path.getType(), path.getMetadata());
    }

    public Quser(PathMetadata metadata) {
        super(user.class, metadata);
    }

}

