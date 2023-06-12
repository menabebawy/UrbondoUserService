package com.urbondo.user.api.service;

public record User(String id,
                   String firstName,
                   String lastName,
                   String email,
                   String phone) {}
