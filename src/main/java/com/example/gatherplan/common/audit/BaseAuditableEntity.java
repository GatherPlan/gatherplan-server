package com.example.gatherplan.common.audit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@ToString(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
//@Table(indexes = {@Index(columnList = "updatedAt")}) // BaseEntity 에 index 설정 가능하면 추가하기 -> 현재 오류
//@Table(indexes = {@Index(columnList = "createdAt")}) // BaseEntity 에 index 설정 가능하면 추가하기 -> 현재 오류
public class BaseAuditableEntity implements Serializable {

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
}
