package com.urbondo.user.api.controller;

public class UserNotFoundException extends UrbondoException {
    public UserNotFoundException(String id) {
        super("User id:" + id + " is not found.");
    }
}
