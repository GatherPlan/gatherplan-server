package com.example.gatherplan.appointment.repository.entity;

import com.example.gatherplan.common.audit.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@Table(name = "TEMP_USER")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TempUser extends BaseAuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEMP_USER_ID")
    private Long id;

    @Comment("사용자 이름")
    @Column(nullable = false)
    private String nickname;

    @Comment("비밀번호")
    @Column(nullable = false)
    private String password;
}
