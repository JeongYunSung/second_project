package com.yunseong.second_project.category.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
public class CategoryUpdateRequest {

    @NotBlank
    @Size(min = 2, max = 10)
    private String categoryName;
    @NotNull
    @Min(0) @Max(Long.MAX_VALUE)
    private Long parentId;
}
