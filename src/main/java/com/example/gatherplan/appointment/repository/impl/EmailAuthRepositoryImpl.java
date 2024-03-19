package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.repository.EmailAuthRepository;
import com.example.gatherplan.appointment.repository.entity.EmailAuth;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.example.gatherplan.appointment.repository.entity.QEmailAuth.emailAuth;

@Repository
public class EmailAuthRepositoryImpl implements EmailAuthRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public EmailAuthRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public void save(EmailAuth emailAuth) {
        entityManager.persist(emailAuth);
    }

    @Override
    public void delete(String email) {
        jpaQueryFactory
                .delete(emailAuth)
                .where(emailAuth.email.eq(email))
                .execute();
    }

    @Override
    public Optional<EmailAuth> findEmailAuthByEmail(String email) {
        EmailAuth result = jpaQueryFactory
                .selectFrom(emailAuth)
                .where(emailAuth.email.eq(email))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
