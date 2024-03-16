package com.example.gatherplan.appointment.repository.entity;

import com.example.gatherplan.appointment.enums.UserAuthType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserAuthType userAuthType;

    private String role;

    @OneToMany(mappedBy = "member")
    private List<Participation> participationEntities;
}
