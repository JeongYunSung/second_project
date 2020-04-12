package com.yunseong.second_project.common.errors;

public class NotCanceledOrder extends RuntimeException {

    public NotCanceledOrder(String message) {
        super(message);
    }
}
