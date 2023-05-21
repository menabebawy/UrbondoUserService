package com.urbondo.user.service.exception;

public class InvalidPhoneException extends RuntimeException{
    public InvalidPhoneException() {
        super("Phone is invalid, please try again with a valid one.");
    }
}
