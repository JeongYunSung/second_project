package com.yunseong.second_project.order.commend.domain;

import com.yunseong.second_project.order.query.OrderProductResponse;
import com.yunseong.second_project.order.query.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o join fetch o.orderItems oi join fetch oi.product pd " +
            "left join fetch o.payment pm where o.id = :id")
    Optional<Order> findById(Long id);

    @Query("select new com.yunseong.second_project.order.query.OrderResponse(o.id, o.totalPrice, p.paymentStatus) from Order o left join o.payment p")
    Page<OrderResponse> findByPage(Pageable pageable);

    @Query("select new com.yunseong.second_project.order.query.OrderProductResponse(oi.order.id, p.productName, p.value) from OrderItem oi join oi.product p" +
            " where oi.order.id in :ids")
    List<OrderProductResponse> findByOrderProductResponse(List<Long> ids);
}
