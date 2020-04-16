package com.yunseong.second_project.order.ui;

import com.yunseong.second_project.common.util.Util;
import com.yunseong.second_project.member.ui.MemberMeController;
import com.yunseong.second_project.order.commend.application.OrderCommendService;
import com.yunseong.second_project.order.query.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/orders/{id}/payment", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class PaymentController {

    private  final OrderCommendService orderCommendService;

    @PostMapping
    public ResponseEntity completePayment(@PathVariable Long id) {
        PaymentResponse paymentResponse = this.orderCommendService.completePayment(id);
        URI uri = linkTo(methodOn(MemberMeController.class).findMember()).toUri();
        return ResponseEntity.created(uri).body((new EntityModel<>(paymentResponse,
                Util.profile)));
    }
}
