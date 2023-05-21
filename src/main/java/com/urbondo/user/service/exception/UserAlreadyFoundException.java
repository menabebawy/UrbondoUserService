package com.urbondo.user.service.exception;

public class UserAlreadyFoundException extends RuntimeException{
    public UserAlreadyFoundException(String email) {
        super("User email:" + email + " is already exist.");
    }
}
