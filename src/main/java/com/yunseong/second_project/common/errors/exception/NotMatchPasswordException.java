package com.yunseong.second_project.common.errors.exception;

public class NotMatchPasswordException extends RuntimeException {

    public NotMatchPasswordException(String message) {
        super(message);
    }
}
