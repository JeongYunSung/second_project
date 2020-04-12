package com.yunseong.second_project.order.commend.application;

import com.yunseong.second_project.common.domain.CustomUser;
import com.yunseong.second_project.member.command.domain.Member;
import com.yunseong.second_project.member.command.domain.MemberPurchase;
import com.yunseong.second_project.member.command.domain.MemberRepository;
import com.yunseong.second_project.member.command.domain.Purchase;
import com.yunseong.second_project.order.commend.application.dto.OrderCreateRequest;
import com.yunseong.second_project.order.commend.domain.Order;
import com.yunseong.second_project.order.commend.domain.OrderItem;
import com.yunseong.second_project.order.commend.domain.OrderRepository;
import com.yunseong.second_project.order.commend.domain.Payment;
import com.yunseong.second_project.product.commend.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderCommendService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public void createOrder(OrderCreateRequest request) {
        Order order = new Order();

        request.getOrderItems().stream().map(id -> new OrderItem(this.productRepository.findById(id).orElseThrow(EntityNotFoundException::new)))
                .forEach(order::addOrderItem);

        this.orderRepository.save(order);
    }

    public void cancleOrder(Long id) {
        Order order = this.orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        order.cancel();
    }

    public void complatePayment(Long id) {
        CustomUser principal = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Member member = this.memberRepository.findPurchaseById(principal.getId()).orElseThrow(EntityNotFoundException::new);

        Order order = this.orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Payment payment = new Payment();
        payment.comp(member.getMoney());

        order.getOrderItems().forEach(orderitem -> member.addPurchase(new MemberPurchase(new Purchase(orderitem.getProduct().getId(), orderitem.getProduct().getProductName()))));

        order.setPayment(payment);
    }
}
