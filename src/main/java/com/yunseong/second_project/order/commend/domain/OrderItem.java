package com.yunseong.second_project.order.commend.domain;

import com.yunseong.second_project.common.domain.BaseUserEntity;
import com.yunseong.second_project.product.commend.domain.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "orderitem_id"))
public class OrderItem extends BaseUserEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_name")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public OrderItem(Product product) {
        this.product = product;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
