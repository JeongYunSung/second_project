package com.yunseong.second_project.product.query.application.dto;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class ProductResponseModel extends EntityModel<ProductResponse> {

    public ProductResponseModel(ProductResponse content, Link... links) {
        super(content, links);
    }
}
