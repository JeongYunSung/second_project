package com.yunseong.second_project.common.ui.advice;

import com.yunseong.second_project.common.errors.NotExistEntityException;
import com.yunseong.second_project.common.errors.NotFoundUserException;
import com.yunseong.second_project.common.errors.NotMatchPasswordException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EntityControllerAdvice {

    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity handleNotFoundUsernameException(NotFoundUserException exception) {
        Errors errors = new BeanPropertyBindingResult(exception.getId(), "유저 고유번호");
        errors.rejectValue("id", "wrongValue", "UserId is wrongValue");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NotMatchPasswordException.class)
    public ResponseEntity handleNotMatchPasswordException(NotMatchPasswordException exception) {
        Errors errors = new BeanPropertyBindingResult(exception.getPassword(), "유저 비밀번호");
        errors.rejectValue("password", "notMatch", "Password not match");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NotExistEntityException.class)
    public ResponseEntity handleNotExistEntityException(NotExistEntityException exception) {
        Errors errors = new BeanPropertyBindingResult(exception.getAClass(), "엔티티");
        errors.reject("notExist", "Entity doesn't Exist");
        return ResponseEntity.badRequest().body(errors);
    }
}
