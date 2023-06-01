package com.urbondo.user.service.controller;

import com.urbondo.user.service.exception.UserAlreadyFoundException;
import com.urbondo.user.service.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
class UserControllerAdvisor {

    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class, UserAlreadyFoundException.class})
    ResponseEntity<ErrorResponse> handleControllerException(Throwable exception) {
        return generateResponseEntity(BAD_REQUEST, exception);
    }


    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception) {
        return generateResponseEntity(NOT_FOUND, exception);
    }

    private ResponseEntity<ErrorResponse> generateResponseEntity(HttpStatus httpStatus, Throwable exception) {
        return new ResponseEntity<>(new ErrorResponse(httpStatus,
                                                      exception.getMessage(),
                                                      Arrays.toString(exception.getStackTrace())), httpStatus);
    }
}
