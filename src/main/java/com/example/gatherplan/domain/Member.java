package com.example.gatherplan.domain;

import com.example.gatherplan.domain.enums.UserAuthType;
import com.example.gatherplan.domain.enums.UserType;
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
    private UserType userType;

    @Enumerated(EnumType.STRING)
    private UserAuthType userAuthType;

    @OneToMany(mappedBy = "member")
    private List<ParticipationEntity> participationEntities;
}
