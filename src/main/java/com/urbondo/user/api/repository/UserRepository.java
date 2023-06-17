package com.urbondo.user.api.repository;

import com.urbondo.lib.UrbondoRepository;

public interface UserRepository extends UrbondoRepository<UserDao> {
    boolean isEmailExist(String email);
}
