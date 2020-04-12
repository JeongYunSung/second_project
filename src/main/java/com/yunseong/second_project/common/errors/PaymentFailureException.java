package com.yunseong.second_project.common.errors;

public class PaymentFailureException extends RuntimeException {

    public PaymentFailureException(String message) {
        super(message);
    }
}
