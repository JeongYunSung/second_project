package com.yunseong.second_project.common.domain;

import com.yunseong.second_project.common.errors.NotExistEntityException;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedTime;

    private boolean isDelete;

    public void delete() {
        this.isDelete();
        this.isDelete = true;
    }

    public void isDelete() {
        if (this.isDelete) {
            throw new NotExistEntityException("해당 엔티티는 삭제된 상태입니다.", BaseEntity.class);
        }
    }
}
