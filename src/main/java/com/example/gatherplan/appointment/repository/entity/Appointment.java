package com.example.gatherplan.appointment.repository.entity;

import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.TimeType;
import com.example.gatherplan.appointment.repository.entity.embedded.ConfirmedDateTime;
import com.example.gatherplan.common.audit.BaseAuditableEntity;
import com.example.gatherplan.common.unit.Address;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Appointment extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPOINTMENT_ID")
    private Long id;

    @Column(nullable = false)
    @Comment("약속 이름")
    private String appointmentName;

    @Embedded
    @Comment("약속 장소")
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("약속 상태 (확정, 미확정)")
    private AppointmentState appointmentState;

    @Embedded
    @Comment("약속 확정 날짜,시간")
    private ConfirmedDateTime confirmedDateTime;

    @ElementCollection
    @Builder.Default
    @Comment("약속 후보 날짜들")
    private List<LocalDate> candidateDateList = new ArrayList<>();

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Comment("약속 후보 시간들 (시작시간~종료시간)")
    private List<TimeType> candidateTimeTypeList = new ArrayList<>();
}
