package com.yunseong.second_project.member.query.dto;

import com.yunseong.second_project.member.command.domain.Purchase;
import lombok.Getter;

@Getter
public class PurchaseResponse {

    private Long productId;
    private String productName;

    public PurchaseResponse(Purchase purchase) {
        this.productId = purchase.getId();
        this.productName = purchase.getProduct().getProductName();
    }
}
