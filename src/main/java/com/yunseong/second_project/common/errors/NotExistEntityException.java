package com.yunseong.second_project.common.errors;

import lombok.Getter;

@Getter
public class NotExistEntityException extends RuntimeException {

    private Class aClass;

    public NotExistEntityException(String message, Class aClass) {
        super(message);
        this.aClass = aClass;
    }
}
