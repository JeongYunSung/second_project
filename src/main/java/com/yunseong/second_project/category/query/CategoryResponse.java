package com.yunseong.second_project.category.query;

import com.querydsl.core.annotations.QueryProjection;
import com.yunseong.second_project.category.command.domain.Category;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CategoryResponse {

    private Long id;
    private String categoryName;
    private Long parentId;
    private String parentName;
    private List<CategoryChild> child;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    @QueryProjection
    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.categoryName = category.getCategoryName();
        if(category.getParent() != null) {
            this.parentId = category.getParent().getId();
            this.parentName = category.getParent().getCategoryName();
        }
        if(category.getCategories() != null && category.getCategories().size() > 1)
            this.child = category.getCategories().stream().map(c -> new CategoryChild(c.getId(), c.getCategoryName())).collect(Collectors.toList());
        this.createdTime = category.getCreatedTime();
        this.updatedTime = category.getUpdatedTime();
    }
}
