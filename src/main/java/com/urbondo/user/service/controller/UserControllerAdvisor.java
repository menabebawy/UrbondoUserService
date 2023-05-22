package com.urbondo.user.service.controller;

import com.urbondo.user.service.exception.InvalidEmailException;
import com.urbondo.user.service.exception.InvalidPhoneException;
import com.urbondo.user.service.exception.UserAlreadyFoundException;
import com.urbondo.user.service.exception.UserNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
class UserControllerAdvisor {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    String handleUserNotFoundException(UserNotFoundException exception) {
        return createResponseBody(exception.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({UserAlreadyFoundException.class, InvalidEmailException.class, InvalidPhoneException.class})
    String handleBadRequestException(Exception exception) {
        return createResponseBody(exception.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    String handleValidationErrors(MethodArgumentNotValidException exception) {
        return createResponseBody(exception.getBindingResult()
                                          .getFieldErrors()
                                          .stream()
                                          .map(FieldError::getDefaultMessage)
                                          .findFirst()
                                          .orElse(""));
    }

    private String createResponseBody(String value) {
        return String.format("{ \"message\": \"%s\" }", value);
    }
}
