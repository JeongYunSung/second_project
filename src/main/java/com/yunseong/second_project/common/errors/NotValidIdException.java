package com.yunseong.second_project.common.errors;

import lombok.Getter;

@Getter
public class NotValidIdException extends RuntimeException {

    private Long id;

    public NotValidIdException(String message, Long id) {
        super(message);
        this.id = id;
    }
}
