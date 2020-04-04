package com.yunseong.second_project.product.application.dto;

import com.yunseong.second_project.product.domain.Product;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProductCreateResponse {

    private Long id;
    private String productName;
    private String description;
    private Integer value;
    private Integer recommend;
    private Integer view;
    private List<TypeResponse> categories;

    public ProductCreateResponse(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.value = product.getValue();
        this.recommend = product.getRecommend();
        this.view = product.getView();
        this.categories = product.getTypes().stream().map(TypeResponse::new).collect(Collectors.toList());
    }
}
