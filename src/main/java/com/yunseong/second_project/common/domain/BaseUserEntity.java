package com.yunseong.second_project.common.domain;

import com.yunseong.second_project.member.command.domain.Member;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseUserEntity extends BaseEntity {

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Long createdMemberId;

    @LastModifiedBy
    @Column(nullable = false)
    private Long updatedMemberId;
}
