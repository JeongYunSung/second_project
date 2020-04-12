package com.yunseong.second_project.order.query;

import com.yunseong.second_project.order.commend.domain.Order;
import com.yunseong.second_project.order.commend.domain.PaymentStatus;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponse {

    private List<OrderProductResponse> orderProductResponses;
    private int totalPrice;
    private PaymentStatus paymentStatus;

    public OrderResponse(Order order) {
        this.orderProductResponses = order.getOrderItems().stream().map(orderItem -> new OrderProductResponse(orderItem.getProduct())).collect(Collectors.toList());
        this.totalPrice = order.getTotalPrice();
        this.paymentStatus = order.getPayment().getPaymentStatus();
    }
}
