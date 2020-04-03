package com.yunseong.second_project.category.command.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunseong.second_project.category.command.domain.Category;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class CategoryUpdateResponse {

    private @With Long id;
    private @With String categoryName;
    private @With Long parentId;
    private @With String parentName;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public CategoryUpdateResponse(Category category) {
        this.createdTime = category.getCreatedTime();
        this.updatedTime = category.getUpdatedTime();
    }
}
