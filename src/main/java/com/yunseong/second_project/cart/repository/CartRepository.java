package com.yunseong.second_project.cart.repository;

import com.yunseong.second_project.cart.domain.CartItem;

import java.util.List;

public interface CartRepository {

    void addItem(String id, CartItem item);

    List<CartItem> findCartItemList(String id);

    boolean removeCartItem(String id, Long idx);

    void deleteCart(String id);
}
