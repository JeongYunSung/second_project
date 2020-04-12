package com.yunseong.second_project.product.commend.domain;

import com.yunseong.second_project.common.domain.BaseUserEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "type_id"))
public class Type extends BaseUserEntity {

    @Version
    private Integer version;

    private Long categoryId;
    private String categoryName;
    private Long parentId;
    private String parentName;

    public Type(Long categoryId, String categoryName) {
        this(categoryId, categoryName, null, null);
    }

    public Type(Long categoryId, String categoryName, Long parentId, String parentName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.parentName = parentName;
    }
}
