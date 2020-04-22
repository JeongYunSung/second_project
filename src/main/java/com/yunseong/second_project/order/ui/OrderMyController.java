package com.yunseong.second_project.order.ui;

import com.yunseong.second_project.common.util.Util;
import com.yunseong.second_project.order.query.OrderQueryService;
import com.yunseong.second_project.order.query.OrderResponse;
import com.yunseong.second_project.order.query.model.OrderResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/orders/my", produces = MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8")
@RequiredArgsConstructor
public class OrderMyController {

    private final OrderQueryService orderQueryService;

    @GetMapping
    public ResponseEntity findsOrder(@PageableDefault Pageable pageable) {
        Page<OrderResponse> page = this.orderQueryService.findsOrder(pageable);
        List<OrderResponseModel> order = page.stream().map(orderResponse -> new OrderResponseModel(orderResponse, linkTo(methodOn(OrderController.class).findOrder(orderResponse.getId())).withRel("order"))).collect(Collectors.toList());
        PagedModel<OrderResponseModel> model = new PagedModel<>(order, new PagedModel.PageMetadata(pageable.getPageSize(), pageable.getPageNumber(), page.getTotalElements()));
        model.add(Util.profile);
        return ResponseEntity.ok(model);
    }
}
