package com.yunseong.second_project.common.ui;

import com.yunseong.second_project.common.domain.BaseEntity;
import com.yunseong.second_project.common.errors.DeletedEntityException;
import com.yunseong.second_project.common.errors.NotFoundEntityException;
import com.yunseong.second_project.common.errors.NotMatchPasswordException;
import com.yunseong.second_project.common.errors.NotValidIdException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EntityControllerAdvice {

    @ExceptionHandler(NotFoundEntityException.class)
    public ResponseEntity handleNotFoundUsernameException(NotFoundEntityException exception) {
        Errors errors = new BeanPropertyBindingResult(exception.getId(), "엔티티 고유번호");
        errors.rejectValue("id", "wrongValue", "EntityId is wrongValue");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NotMatchPasswordException.class)
    public ResponseEntity handleNotMatchPasswordException(NotMatchPasswordException exception) {
        Errors errors = new BeanPropertyBindingResult(exception.getPassword(), "유저 비밀번호");
        errors.rejectValue("password", "notMatch", "Password not match");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DeletedEntityException.class)
    public ResponseEntity handleNotExistEntityException(DeletedEntityException exception) {
        Errors errors = new BeanPropertyBindingResult(BaseEntity.class, "엔티티");
        errors.reject("deleted", "Entity is deleted");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NotValidIdException.class)
    public ResponseEntity handleNotValidIdException(NotValidIdException exception) {
        Errors errors = new BeanPropertyBindingResult(exception.getId(), "아이디");
        errors.rejectValue("id", "notValid", "Id is notValid");
        return ResponseEntity.badRequest().body(errors);
    }
}
