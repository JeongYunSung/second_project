package com.yunseong.second_project.order.commend.domain;

import com.yunseong.second_project.common.domain.BaseUserEntity;
import com.yunseong.second_project.common.errors.NotCanceledOrder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "orders")
@AttributeOverride(name = "id", column = @Column(name = "orders_id"))
public class Order extends BaseUserEntity {

    private int totalPrice;

    @Version
    private Integer version;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Payment payment;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    public Order() {
        this.orderStatus = OrderStatus.ORDER;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
        payment.setOrder(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        this.getOrderItems().add(orderItem);
        orderItem.setOrder(this);
        this.totalPrice += orderItem.getProduct().getValue();
    }

    public void cancel() {
        if (this.getOrderStatus() == OrderStatus.CANCEL) {
            throw new NotCanceledOrder("해당 주문은 이미 취소된 상태입니다");
        }
        this.orderStatus = OrderStatus.CANCEL;
    }
}
