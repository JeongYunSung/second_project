package com.yunseong.second_project.order.commend.domain;

import com.yunseong.second_project.common.domain.BaseUserEntity;
import com.yunseong.second_project.common.errors.PaymentFailureException;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@AttributeOverride(name = "id", column = @Column(name = "payment_id"))
public class Payment extends BaseUserEntity {

    @OneToOne(fetch = FetchType.LAZY)
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public Payment() {
        this.paymentStatus = PaymentStatus.READY;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void comp(int ownPrice) {
        if (this.paymentStatus != PaymentStatus.COMP) {
            throw new PaymentFailureException("해당 주문은 이미 결제된 상태입니다.");
        }
        if (this.getOrder().getTotalPrice() > ownPrice) {
            throw new PaymentFailureException("잔액이 부족합니다.");
        }
        this.paymentStatus = PaymentStatus.COMP;
    }
}
