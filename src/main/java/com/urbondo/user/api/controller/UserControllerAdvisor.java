package com.urbondo.user.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
class UserControllerAdvisor {

    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class, UserAlreadyFoundException.class})
    public ResponseEntity<ErrorResponse> handleControllerException(UrbondoException exception) {
        return generateResponseEntity(BAD_REQUEST, exception);
    }


    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception) {
        return generateResponseEntity(NOT_FOUND, exception);
    }

    private ResponseEntity<ErrorResponse> generateResponseEntity(HttpStatus httpStatus, UrbondoException exception) {
        return new ResponseEntity<>(new ErrorResponse(httpStatus, exception.getMessage()), httpStatus);
    }
}
