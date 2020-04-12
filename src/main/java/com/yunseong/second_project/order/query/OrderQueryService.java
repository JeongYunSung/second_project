package com.yunseong.second_project.order.query;

import com.yunseong.second_project.order.commend.domain.Order;
import com.yunseong.second_project.order.commend.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService {

    private OrderRepository orderRepository;

    public OrderResponse findOrder(Long id) {
        Order order = this.orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return new OrderResponse(order);
    }
}
