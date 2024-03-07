package com.example.gatherplan.appointment.repository.entity;

import com.example.gatherplan.appointment.repository.entity.embedded.Address;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.CandidateTimeType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPOINTMENT_ID")
    private Long id;

    private String name;

    private String description;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private AppointmentState appointmentState;

    private LocalDate confirmedDate;
    private LocalTime confirmedStartTime;
    private LocalTime confirmedEndTime;

    @ElementCollection
    @Builder.Default
    private List<LocalDate> candidateDates = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private CandidateTimeType candidateTimeType;

    @ElementCollection
    @Builder.Default
    private List<LocalTime> candidateTimes = new ArrayList<>();

    @OneToMany(mappedBy = "appointment")
    private List<ParticipationEntity> participationEntities;
}
