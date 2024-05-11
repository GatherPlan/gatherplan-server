package com.example.gatherplan.appointment.repository.entity;

import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.common.audit.BaseAuditableEntity;
import com.example.gatherplan.common.unit.Address;
import com.example.gatherplan.common.unit.ConfirmedDateTime;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"APPOINTMENT_CODE"})})
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

    @Column(nullable = false)
    @Comment("약속 코드")
    private String appointmentCode;

    @ElementCollection
    @Builder.Default
    @Comment("약속 후보 날짜들")
    private List<LocalDate> candidateDateList = new ArrayList<>();

    private String notice;

    public void update(String appointmentName, Address address, List<LocalDate> candidateDateList, String notice) {
        this.appointmentName = appointmentName;
        this.address = address;
        this.notice = notice;
        this.candidateDateList = List.copyOf(candidateDateList);
    }

    public void confirmed(ConfirmedDateTime confirmedDateTime) {
        this.confirmedDateTime = confirmedDateTime;
    }

}
