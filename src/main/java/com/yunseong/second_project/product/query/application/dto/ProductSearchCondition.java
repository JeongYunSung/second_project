package com.yunseong.second_project.product.query.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductSearchCondition {

    private String productName;
    private String categoryName;
    private Integer min;
    private Integer max;
}
