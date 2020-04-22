package com.yunseong.second_project.product.query.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchCondition {

    private String product;
    private String category;
    private String min;
    private String max;
}
