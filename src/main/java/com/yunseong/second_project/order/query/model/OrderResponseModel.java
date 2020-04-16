package com.yunseong.second_project.order.query.model;

import com.yunseong.second_project.order.query.OrderResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class OrderResponseModel extends EntityModel<OrderResponse> {

    public OrderResponseModel(OrderResponse content, Link... links) {
        super(content, links);
    }
}
