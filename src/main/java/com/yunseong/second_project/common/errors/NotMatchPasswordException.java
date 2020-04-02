package com.yunseong.second_project.common.errors;

import lombok.Getter;

@Getter
public class NotMatchPasswordException extends RuntimeException {

    private String password;

    public NotMatchPasswordException(String message, String password) {
        super(message);
        this.password = password;
    }
}
