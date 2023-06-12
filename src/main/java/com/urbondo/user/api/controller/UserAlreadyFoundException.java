package com.urbondo.user.api.controller;

public class UserAlreadyFoundException extends UrbondoException {
    public UserAlreadyFoundException(String email) {
        super("User email:" + email + " is already exist.");
    }
}
