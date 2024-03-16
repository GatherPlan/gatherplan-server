package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.repository.entity.EmailAuth;
import com.example.gatherplan.appointment.repository.entity.Member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findMemberByEmail(String email);

    void saveEmailAuth(EmailAuth emailAuth);

    Optional<EmailAuth> findEmailAuthByEmail(String email);

    void deleteEmailAuth(String email);

    Optional<Member> findMemberByName(String name);

    void saveMember(Member member);
}
