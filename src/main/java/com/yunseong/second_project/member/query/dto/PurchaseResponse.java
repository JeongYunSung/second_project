package com.yunseong.second_project.member.query.dto;

import com.yunseong.second_project.member.command.domain.MemberPurchase;
import com.yunseong.second_project.member.command.domain.Purchase;
import lombok.Getter;

@Getter
public class PurchaseResponse {

    private Long productId;
    private String productName;

    public PurchaseResponse(MemberPurchase purchase) {
        this.productId = purchase.getPurchase().getId();
        this.productName = purchase.getPurchase().getProductName();
    }
}
