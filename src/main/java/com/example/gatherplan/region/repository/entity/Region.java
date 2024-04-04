package com.example.gatherplan.region.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("지역 이름")
    @Column(nullable = false)
    private String address;

    @Comment("지역 코드")
    @Column(nullable = false)
    private String code;
}
