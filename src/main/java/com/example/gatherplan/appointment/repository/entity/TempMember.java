package com.example.gatherplan.appointment.repository.entity;

import com.example.gatherplan.common.audit.BaseAuditableEntity;
import com.example.gatherplan.common.jwt.RoleType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@Table(name = "TEMP_MEMBER")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TempMember extends BaseAuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEMP_MEMBER_ID")
    private Long id;

    @Comment("사용자 이름")
    @Column(nullable = false)
    private String nickname;

    @Comment("비밀번호")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Comment("Security 에 사용되는 Role")
    private RoleType roleType;

    public String getRole() {
        return this.roleType.getRole();
    }
}
