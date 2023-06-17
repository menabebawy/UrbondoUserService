package com.urbondo.user.api.service;

import com.urbondo.user.api.controller.AddUserRequestDto;
import com.urbondo.user.api.controller.UpdateUserRequestDto;
import com.urbondo.user.api.repository.UserDao;

public interface UserService {
    UserDao findById(String id);

    UserDao add(AddUserRequestDto requestDTO);

    UserDao updateById(UpdateUserRequestDto updateUserRequestDTO);

    void deleteBy(String id);
}