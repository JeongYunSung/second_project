package com.yunseong.second_project.common.errors;

import lombok.Getter;

@Getter
public class DeletedEntityException extends RuntimeException {

    public DeletedEntityException(String message) {
        super(message);
    }
}
