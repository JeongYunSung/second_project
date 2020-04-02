package com.yunseong.second_project.common.errors;

import lombok.Getter;

@Getter
public class NotFoundUserException extends RuntimeException {

    private Long id;

    public NotFoundUserException(String message, Long id) {
        super(message);
        this.id = id;
    }
}
