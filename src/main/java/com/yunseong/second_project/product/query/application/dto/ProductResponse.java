package com.yunseong.second_project.product.query.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.yunseong.second_project.common.domain.CustomUser;
import com.yunseong.second_project.product.commend.domain.Product;
import com.yunseong.second_project.product.commend.domain.Referee;
import lombok.Getter;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProductResponse {

    private Long productId;
    private String productName;
    private String description;
    private Integer value;
    private Integer view;
    private List<RecommendResponse> recommends;
    private List<TypeResponse> types;
    private String memberId;

    @QueryProjection
    public ProductResponse(Product product) {
        this.productId = product.getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.value = product.getValue();
        this.view = product.getView();
        this.recommends = product.getProductReferees().stream().map(
                referee -> {
                Referee r = referee.getReferee();
                return new RecommendResponse(r.getUsername(), r.getNickname());
                }).collect(Collectors.toList());
        this.types = product.getTypes().stream().map(type -> new TypeResponse(type.getType())).collect(Collectors.toList());
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof CustomUser) {
            this.memberId = ((CustomUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        }
    }
}
