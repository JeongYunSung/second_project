package com.yunseong.second_project.order.commend.application;

import com.yunseong.second_project.common.domain.CustomUser;
import com.yunseong.second_project.common.errors.NotCanceledOrder;
import com.yunseong.second_project.member.command.domain.Member;
import com.yunseong.second_project.member.command.domain.MemberPurchase;
import com.yunseong.second_project.member.command.domain.MemberRepository;
import com.yunseong.second_project.member.command.domain.Purchase;
import com.yunseong.second_project.order.commend.application.dto.OrderCreateRequest;
import com.yunseong.second_project.order.commend.domain.*;
import com.yunseong.second_project.order.query.PaymentResponse;
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

    public Long createOrder(OrderCreateRequest request) {
        Order order = new Order();

        request.getOrderItems().stream().map(id -> new OrderItem(this.productRepository.findById(id).orElseThrow(EntityNotFoundException::new)))
                .forEach(order::addOrderItem);

        this.orderRepository.save(order);

        return order.getId();
    }

    public void cancelOrder(Long id) {
        Order order = this.orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        order.cancel();
    }

    public PaymentResponse completePayment(Long id) {
        CustomUser principal = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Member member = this.memberRepository.findPurchaseById(principal.getId()).orElseThrow(EntityNotFoundException::new);

        Order order = this.orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Payment payment = new Payment();
        order.setPayment(payment);
        payment.comp(member.getMoney());

        member.subMoney(order.getTotalPrice());

        order.getOrderItems().forEach(orderitem -> member.addPurchase(new MemberPurchase(new Purchase(orderitem.getProduct().getId(), orderitem.getProduct().getProductName()))));

        return new PaymentResponse(payment);
    }
}
