package com.yunseong.second_project.product.application.dto;

import com.yunseong.second_project.product.domain.Type;
import lombok.Getter;

@Getter
public class TypeResponse {

    private Long categoryId;
    private String categoryName;
    private Long parentId;
    private String parentName;

    public TypeResponse(Type type) {
        this.categoryId = type.getCategoryId();
        this.categoryName = type.getCategoryName();
        this.parentId = type.getParentId();
        this.parentName = type.getParentName();
    }
}
