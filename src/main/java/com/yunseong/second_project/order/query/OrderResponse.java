package com.yunseong.second_project.order.query;

import com.yunseong.second_project.order.commend.domain.Order;
import com.yunseong.second_project.order.commend.domain.OrderStatus;
import com.yunseong.second_project.order.commend.domain.PaymentStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode(of = "id")
public class OrderResponse {

    private Long id;
    private List<OrderProductResponse> orderProductResponses;
    private int totalPrice;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.orderProductResponses = order.getOrderItems().stream().map(orderItem -> new OrderProductResponse(orderItem.getProduct())).collect(Collectors.toList());
        this.totalPrice = order.getTotalPrice();
        this.orderStatus = order.getOrderStatus();
        if(order.getPayment() != null)
            this.paymentStatus = order.getPayment().getPaymentStatus();
        else
            this.paymentStatus = PaymentStatus.READY;
    }

    public OrderResponse(Long id, int totalPrice, OrderStatus orderStatus, PaymentStatus paymentStatus) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
    }

    public void setOrderProductResponses(List<OrderProductResponse> orderProductResponses) {
        this.orderProductResponses = orderProductResponses;
    }
}
