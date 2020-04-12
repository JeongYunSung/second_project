package com.yunseong.second_project.common.ui;

import com.yunseong.second_project.common.errors.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EntityControllerAdvice {

    @ExceptionHandler(NotFoundEntityException.class)
    public ResponseEntity handleNotFoundUsernameException(NotFoundEntityException exception) {
        Errors errors = new BeanPropertyBindingResult(exception.getId(), "Entity Id");
        errors.rejectValue("id", "wrongValue", "EntityId is wrongValue");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NotMatchPasswordException.class)
    public ResponseEntity handleNotMatchPasswordException(NotMatchPasswordException exception) {
        Errors errors = new BeanPropertyBindingResult(exception.getPassword(), "User Password");
        errors.rejectValue("password", "notMatch", "Password not match");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NotMatchUserException.class)
    public ResponseEntity handleNotMatchUserException(NotMatchUserException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("notMatch", "not Match Id");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NotValidIdException.class)
    public ResponseEntity handleNotValidIdException(NotValidIdException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("notValid", "Id is notValid");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleDataIntegrityViolationException(DataIntegrityViolationException excepton) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("reduplication", "Entity is reduplication");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NotCanceledOrder.class)
    public ResponseEntity handleNotCanceledOrderException(NotCanceledOrder exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("canceled", "Order is canceled");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NotCanceledPayment.class)
    public ResponseEntity handleNotCanceledPaymentException(NotCanceledPayment exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("canceled", "Payment is canceled");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(PaymentFailureException.class)
    public ResponseEntity handlePaymentFailureExceptionException(PaymentFailureException exception) {
        Errors errors = new BeanPropertyBindingResult(null, "");
        errors.reject("failure", exception.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }
}
