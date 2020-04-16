package com.yunseong.second_project.cart.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private Long productId;
    private String title;
    private String description;
    private int value;
}
