package com.urbondo.user.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class UserControllerAdvisor {

    @ResponseBody
    @ExceptionHandler({UserAlreadyFoundException.class, UserNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleControllerException(UrbondoException exception) {
        return new ResponseEntity<>(new ErrorResponse(BAD_REQUEST, exception.getMessage()), BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleControllerException(MethodArgumentNotValidException exception) {
        return new ResponseEntity<>(new ErrorResponse(BAD_REQUEST, exception.getMessage()), BAD_REQUEST);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class})
    public void handleResourceNotFoundException() {
    }
}
