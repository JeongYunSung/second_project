package com.yunseong.second_project.product.commend.application.dto;

import com.yunseong.second_project.product.commend.domain.Product;
import com.yunseong.second_project.product.query.application.dto.TypeResponse;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProductCreateResponse {

    private Long id;
    private String productName;
    private String description;
    private Integer value;
    private Integer view;
    private List<String> recommend;
    private List<TypeResponse> categories;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public ProductCreateResponse(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.value = product.getValue();
        this.recommend = product.getProductReferees().stream().map(referee -> referee.getReferee().getUsername()).collect(Collectors.toList());
        this.view = product.getView();
        this.categories = product.getTypes().stream().map(TypeResponse::new).collect(Collectors.toList());
        this.createdTime = product.getCreatedTime();
        this.updatedTime = product.getUpdatedTime();
    }
}
