package com.example.gatherplan.domain;

import com.example.gatherplan.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class ParticipationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "APPOINTMENT_ID")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ElementCollection
    private List<LocalDateTime> selectedDateTime;

}
