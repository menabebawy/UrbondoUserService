package com.urbondo.user.service.exception;

public class InvalidEmailException extends RuntimeException{
    public InvalidEmailException() {
        super("Email is not valid, please try again with a valid one.");
    }
}
