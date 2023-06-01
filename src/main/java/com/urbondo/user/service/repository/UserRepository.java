package com.urbondo.user.service.repository;

public interface UserRepository {
    UserDAO findById(final String id);

    boolean isEmailExist(final String email);

    void save(final UserDAO userDAO);

    void delete(final UserDAO userDAO);
}
