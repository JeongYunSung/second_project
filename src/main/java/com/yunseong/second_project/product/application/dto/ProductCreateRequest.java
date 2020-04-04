package com.yunseong.second_project.product.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProductCreateRequest {

    private String productName;
    private String description;
    private Integer value;
    private List<Long> types;
}
