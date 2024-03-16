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
public class TempMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEMP_MEMBER_ID")
    private Long id;

    private String name;
    private String password;

    private String role;

    @OneToMany(mappedBy = "tempMember")
    private List<TempParticipation> tempParticipationList;
}
