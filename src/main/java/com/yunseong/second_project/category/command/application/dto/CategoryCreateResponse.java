package com.yunseong.second_project.category.command.application.dto;

import com.yunseong.second_project.category.command.domain.Category;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CategoryCreateResponse {

    private Long id;
    private String categoryName;
    private Long parentId;
    private String parentName;
    private LocalDateTime createdTime;

    public CategoryCreateResponse(Category category) {
        this.id = category.getId();
        this.categoryName = category.getCategoryName();
        this.parentId = category.getParent().getId();
        this.parentName = category.getParent().getCategoryName();
        this.createdTime = category.getCreatedTime();
    }
}
