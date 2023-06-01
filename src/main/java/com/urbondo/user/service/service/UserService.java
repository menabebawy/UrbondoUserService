package com.urbondo.user.service.service;

public interface UserService {
    User findById(final String id);

    String add(final AddUser user);

    User updateById(final String id, final UpdateUser updateUser);

    void deleteBy(final String id);
}