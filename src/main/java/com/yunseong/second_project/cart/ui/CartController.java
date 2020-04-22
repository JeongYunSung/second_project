package com.yunseong.second_project.cart.ui;

import com.yunseong.second_project.cart.application.CartDto;
import com.yunseong.second_project.cart.application.CartService;
import com.yunseong.second_project.cart.domain.CartItem;
import com.yunseong.second_project.common.domain.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/carts", produces = MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity findItem() {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CartDto cartDto = this.cartService.findCart(user.getUsername());
        return ResponseEntity.ok(new EntityModel<>(cartDto));
    }

    @PutMapping("/items/new")
    public ResponseEntity addItem(@RequestBody CartItem cartItem) {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.cartService.addItem(user.getUsername(), cartItem);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/items/{id}")
    public ResponseEntity removeItem(@PathVariable Long id) {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean remove = this.cartService.removeItem(user.getUsername(), id);
        return ResponseEntity.ok(new EntityModel<>(remove));
    }

    @DeleteMapping
    public ResponseEntity deleteCart() {
        CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.cartService.deleteCart(user.getUsername());
        return ResponseEntity.noContent().build();
    }
}
