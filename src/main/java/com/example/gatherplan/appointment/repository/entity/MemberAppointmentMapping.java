package com.example.gatherplan.appointment.repository.entity;

import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.common.audit.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBER_APPIONTMENT_MAPPING")
public class MemberAppointmentMapping extends BaseAuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Comment("Appointment Seq(PK)")
    private Long appointmentSeq;

    @Column(nullable = false)
    @Comment("Member Seq(PK)")
    private Long memberSeq;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ElementCollection
    private List<LocalDateTime> selectedDateTime;

}
