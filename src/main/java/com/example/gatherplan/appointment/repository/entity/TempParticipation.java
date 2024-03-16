package com.example.gatherplan.appointment.repository.entity;

import com.example.gatherplan.appointment.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TempParticipation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "APPOINTMENT_ID")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "MEMBER_TEMP_ID")
    private MemberTemp memberTemp;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ElementCollection
    private List<LocalDateTime> selectedDateTime;

}
