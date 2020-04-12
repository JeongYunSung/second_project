package com.yunseong.second_project.common.errors;

public class NotCanceledPayment extends RuntimeException {

    public NotCanceledPayment(String message) {
        super(message);
    }
}
