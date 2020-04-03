package com.yunseong.second_project.category.query;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class CategoryResponseModel extends EntityModel<CategoryResponse> {

    public CategoryResponseModel(CategoryResponse content, Link... links) {
        super(content, links);
    }
}
