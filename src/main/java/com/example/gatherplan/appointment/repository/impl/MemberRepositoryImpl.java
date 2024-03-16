package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.repository.MemberRepository;
import com.example.gatherplan.appointment.repository.entity.EmailAuth;
import com.example.gatherplan.appointment.repository.entity.Member;
import com.example.gatherplan.appointment.repository.entity.TempMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.example.gatherplan.appointment.repository.entity.QEmailAuth.emailAuth;
import static com.example.gatherplan.appointment.repository.entity.QMember.member;


@Repository
public class MemberRepositoryImpl implements MemberRepository {
    private final EntityManager entityManager;

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public MemberRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<Member> findMemberByEmail(String email) {
        Member result = jpaQueryFactory
                .selectFrom(member)
                .where(member.email.eq(email))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public void saveEmailAuth(EmailAuth emailAuth) {
        entityManager.persist(emailAuth);
    }

    @Override
    public Optional<EmailAuth> findEmailAuthByEmail(String email) {
        EmailAuth result = jpaQueryFactory
                .selectFrom(emailAuth)
                .where(emailAuth.userEmail.eq(email))
                .fetchOne();

        return Optional.ofNullable(result);

    }

    @Override
    public void deleteEmailAuth(String email) {
        jpaQueryFactory
                .delete(emailAuth)
                .where(emailAuth.userEmail.eq(email))
                .execute();
    }

    @Override
    public Optional<Member> findMemberByName(String name) {
        Member result = jpaQueryFactory
                .selectFrom(member)
                .where(member.name.eq(name))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public void saveMember(Member member) {
        entityManager.persist(member);
    }

    @Override
    public void saveTemporaryMember(TempMember tempMember) {
        entityManager.persist(tempMember);
    }

}
