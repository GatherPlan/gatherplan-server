package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.repository.entity.Member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findMemberByEmail(String email);

    Optional<Member> findMemberByName(String name);

    void save(Member member);
}
