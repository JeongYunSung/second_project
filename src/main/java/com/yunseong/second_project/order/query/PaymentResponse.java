package com.yunseong.second_project.order.query;

import com.yunseong.second_project.order.commend.domain.Payment;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PaymentResponse {

    private List<String> itemsName;
    private int totalPrice;

    public PaymentResponse(Payment payment) {
        if (payment.getOrder().getOrderItems() != null && payment.getOrder().getOrderItems().size() > 0) {
            this.itemsName = new ArrayList<>();
            payment.getOrder().getOrderItems().forEach(orderItem -> this.itemsName.add(orderItem.getProduct().getProductName()));
        }
       this.totalPrice = payment.getOrder().getTotalPrice();
    }
}
