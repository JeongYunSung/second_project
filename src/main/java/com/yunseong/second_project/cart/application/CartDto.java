package com.yunseong.second_project.cart.application;

import com.yunseong.second_project.cart.domain.CartItem;
import lombok.Getter;

import java.util.List;

@Getter
public class CartDto {

    private String id;
    private List<CartItem> cartItems;
    private int totalMoney;

    public CartDto(String id, List<CartItem> cartItems) {
        this.id = id;
        this.cartItems = cartItems;
        if(this.cartItems != null)
            this.cartItems.stream().forEach(cartItem -> this.totalMoney += cartItem.getValue());
    }
}
