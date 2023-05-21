package com.urbondo.user.service.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String id) {
        super("User id:" + id + " is not found.");
    }
}
