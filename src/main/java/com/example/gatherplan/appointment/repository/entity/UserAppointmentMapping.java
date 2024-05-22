package com.example.gatherplan.appointment.repository.entity;

import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.common.audit.BaseAuditableEntity;
import com.example.gatherplan.common.unit.SelectedDateTime;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER_APPIONTMENT_MAPPING")
public class UserAppointmentMapping extends BaseAuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Comment("Appointment Seq(PK)")
    private Long appointmentSeq;

    @Comment("User Seq(PK)")
    private Long userSeq;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ElementCollection
    private List<SelectedDateTime> selectedDateTimeList;

    private String nickname;

    private boolean isAvailable;

    private String tempPassword;

    private UserAuthType userAuthType;

    public void updateIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void update(List<SelectedDateTime> selectedDateTimeList) {
        this.selectedDateTimeList = List.copyOf(selectedDateTimeList);
    }
}
