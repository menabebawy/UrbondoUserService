package com.urbondo.user.service.service;

import com.urbondo.user.service.model.AddUserRequestDTO;
import com.urbondo.user.service.model.UpdateUserRequestDTO;
import com.urbondo.user.service.model.UserDTO;

public interface UserService {
    UserDTO findById(String id);

    void add(final AddUserRequestDTO requestDTO);

    UserDTO updateBy(final UpdateUserRequestDTO requestDTO);

    void deleteBy(String id);
}
