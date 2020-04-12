package com.yunseong.second_project.order.query;

import com.yunseong.second_project.order.commend.domain.Order;
import com.yunseong.second_project.order.commend.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public OrderResponse findOrder(Long id) {
        Order order = this.orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return new OrderResponse(order);
    }

    public Page<OrderResponse> findsOrder(Pageable pageable) {
        Page<OrderResponse> byPage = this.orderRepository.findByPage(pageable);

        List<Long> ids = byPage.getContent().stream().map(OrderResponse::getId).collect(Collectors.toList());

        List<OrderProductResponse> prlist = this.orderRepository.findByOrderProductResponse(ids);

        Map<Long, List<OrderProductResponse>> map = prlist.stream().collect(Collectors.groupingBy(OrderProductResponse::getId));

        byPage.getContent().forEach(orderResponse -> orderResponse.setOrderProductResponses(map.get(orderResponse.getId())));

        return byPage;
    }
}
