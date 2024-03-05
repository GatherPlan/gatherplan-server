package com.example.gatherplan.domain;

import com.example.gatherplan.domain.embedded.Address;
import com.example.gatherplan.domain.enums.AppointmentState;
import com.example.gatherplan.domain.enums.CandidateTimeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
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
    private List<LocalDate> candidateDates = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private CandidateTimeType candidateTimeType;

    @ElementCollection
    private List<LocalTime> candidateTimes = new ArrayList<>();

    @OneToMany(mappedBy = "appointment")
    private List<ParticipationEntity> participationEntities;
}
