package com.yunseong.second_project.order.ui;

import com.yunseong.second_project.common.util.Util;
import com.yunseong.second_project.order.commend.application.OrderCommendService;
import com.yunseong.second_project.order.commend.application.dto.OrderCreateRequest;
import com.yunseong.second_project.order.query.OrderQueryService;
import com.yunseong.second_project.order.query.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/orders", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class OrderController {

    private final OrderCommendService orderCommendService;
    private final OrderQueryService orderQueryService;

    @PostMapping
    public ResponseEntity registerOrder(@RequestBody OrderCreateRequest request) {
        Long order = this.orderCommendService.createOrder(request);
        URI uri = linkTo(methodOn(OrderController.class).findOrder(order)).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity cancelOrder(@PathVariable Long id) {
        this.orderCommendService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity findOrder(@PathVariable Long id) {
        OrderResponse order = this.orderQueryService.findOrder(id);
        return ResponseEntity.ok(new EntityModel<>(order,
                linkTo(OrderMyController.class).withRel("list"),
                Util.profile));
    }
}
