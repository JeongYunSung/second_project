package com.yunseong.second_project.cart.application;

import com.yunseong.second_project.cart.domain.CartItem;
import com.yunseong.second_project.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public CartDto findCart(String id) {
        List<CartItem> cartItemList = this.cartRepository.findCartItemList(id);
        return new CartDto(id, cartItemList);
    }

    public void addItem(String id, CartItem cartItem) {
        this.cartRepository.addItem(id, cartItem);
    }

    public boolean removeItem(String id, Long idx) {
        return this.cartRepository.removeCartItem(id, idx);
    }

    public void deleteCart(String id) {
        this.cartRepository.deleteCart(id);
    }
}
