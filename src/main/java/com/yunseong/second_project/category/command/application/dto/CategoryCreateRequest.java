package com.yunseong.second_project.category.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class CategoryCreateRequest {

    @NotBlank
    @Min(value = 2) @Max(value = 14)
    private String categoryName;
    @Min(value = 1) @Max(value = Long.MAX_VALUE)
    private Long parentId;
}
