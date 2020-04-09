package com.yunseong.second_project.common.errors;

public class NotMatchUserException extends RuntimeException {

    public NotMatchUserException(String message) {
        super(message);
    }
}
