package com.urbondo.user.service.controller;

import com.urbondo.user.service.exception.InvalidEmailException;
import com.urbondo.user.service.exception.InvalidPhoneException;
import com.urbondo.user.service.exception.UserAlreadyFoundException;
import com.urbondo.user.service.exception.UserNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class UserControllerAdvisor {
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    String handleUserNotFoundException(Exception exception) {
        return createErrorMessage(exception.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({UserAlreadyFoundException.class, InvalidEmailException.class, InvalidPhoneException.class})
    String handleBadRequestException(Exception exception) {
        return createErrorMessage(exception.getMessage());
    }

    private String createErrorMessage(String value) {
        return String.format("{ \"message\": \"%s\" }", value);
    }
}
