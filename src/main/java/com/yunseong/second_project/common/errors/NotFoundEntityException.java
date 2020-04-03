package com.yunseong.second_project.common.errors;

import lombok.Getter;

@Getter
public class NotFoundEntityException extends RuntimeException {

    private Long id;

    public NotFoundEntityException(String message, Long id) {
        super(message);
        this.id = id;
    }
}
