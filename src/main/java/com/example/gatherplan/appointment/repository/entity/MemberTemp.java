package com.example.gatherplan.appointment.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberTemp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_TEMP_ID")
    private Long id;

    private String name;
    private String password;

    private String role;

    @OneToMany(mappedBy = "memberTemp")
    private List<TempParticipation> tempParticipationList;
}
