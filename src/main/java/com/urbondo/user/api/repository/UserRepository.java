package com.urbondo.user.api.repository;

public interface UserRepository {
    UserDAO findById(String id);

    boolean isEmailExist(String email);

    void save(UserDAO userDAO);

    void delete(UserDAO userDAO);
}
