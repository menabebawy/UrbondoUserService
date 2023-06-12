package com.urbondo.user.api.service;

import com.urbondo.user.api.controller.AddUserRequestDTO;
import com.urbondo.user.api.controller.UpdateUserRequestDTO;

public interface UserService {
    User findById(String id);

    User add(AddUserRequestDTO requestDTO);

    User updateById(UpdateUserRequestDTO updateUserRequestDTO);

    void deleteBy(String id);
}