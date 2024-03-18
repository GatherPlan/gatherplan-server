package com.example.gatherplan.appointment.repository.entity;

import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.CandidateTimeType;
import com.example.gatherplan.common.audit.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.time.LocalTime;
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
    private String name;

    @Comment("약속 안내 사항")
    private String notice;

    @Comment("약속 장소")
    private String place; // String 이 아닌 Custom 객체로.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("약속 상태 (확정, 미확정)")
    private AppointmentState appointmentState;

    // 아래 3가지 모두 객체로 관리 필요.
    private LocalDate confirmedDate;
    private LocalTime confirmedStartTime;
    private LocalTime confirmedEndTime;

    @ElementCollection
    @Builder.Default
    private List<LocalDate> candidateDates = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("약속 지정 시간 타입 (직접 입력, 선택 입력)")
    private CandidateTimeType candidateTimeType;

    @ElementCollection
    @Builder.Default
    private List<LocalTime> candidateStartTimes = new ArrayList<>();

    @ElementCollection
    @Builder.Default
    private List<LocalTime> candidateEndTimes = new ArrayList<>();
}
