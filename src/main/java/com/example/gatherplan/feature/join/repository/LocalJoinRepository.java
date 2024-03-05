package com.example.gatherplan.feature.join.repository;

import com.example.gatherplan.domain.EmailAuth;
import com.example.gatherplan.domain.Member;

import java.util.Optional;

public interface LocalJoinRepository {
    Optional<Member> findMemberByEmail(String email);

    void saveEmailAuth(EmailAuth emailAuth);

    Optional<EmailAuth> findEmailAuthByEmail(String email);

    void deleteEmailAuth(String email);

    Optional<Member> findMemberByName(String name);

    void saveMember(Member member);
}
