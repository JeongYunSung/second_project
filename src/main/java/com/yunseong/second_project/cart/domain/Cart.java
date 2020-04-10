package com.yunseong.second_project.cart.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.List;

@Getter
@RedisHash("cart")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Cart {

    @Id
    private String id;

    private List<CartItem> cartItems;

    private Cart(String id, List<CartItem> cartItems) {
        this.id = id;
        this.cartItems = cartItems;
    }

    public static Cart of(String id, List<CartItem> cartItems) {
        return new Cart(id, cartItems);
    }
}
