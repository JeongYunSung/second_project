package com.yunseong.second_project.order.commend.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o inner join fetch o.orderItems oi inner join fetch oi.product pd " +
            "inner join fetch o.payment pm")
    Optional<Order> findById(Long id);
}
