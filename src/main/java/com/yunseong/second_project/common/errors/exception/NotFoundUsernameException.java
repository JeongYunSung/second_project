package com.yunseong.second_project.common.errors.exception;

public class NotFoundUsernameException extends RuntimeException {

    public NotFoundUsernameException(String message) {
        super(message);
    }
}
