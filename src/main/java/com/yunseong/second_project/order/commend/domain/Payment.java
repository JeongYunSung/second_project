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
    @JoinColumn(name = "order_name")
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
        if (this.paymentStatus == PaymentStatus.COMP) {
            throw new PaymentFailureException("payment is completed");
        }
        if (this.getOrder().getTotalPrice() > ownPrice) {
            throw new PaymentFailureException(this.getOrder().getTotalPrice() + " > " + ownPrice);
        }
        this.paymentStatus = PaymentStatus.COMP;
    }
}
