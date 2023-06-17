package com.urbondo.user.api.service;

import com.urbondo.lib.ResourceNotFoundException;
import com.urbondo.user.api.controller.AddUserRequestDto;
import com.urbondo.user.api.controller.UpdateUserRequestDto;
import com.urbondo.user.api.controller.UserAlreadyFoundException;
import com.urbondo.user.api.controller.UserNotFoundException;
import com.urbondo.user.api.repository.UserDao;
import com.urbondo.user.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDao findById(String id) {
        Optional<UserDao> userDAO = userRepository.findById(id);
        if (userDAO.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        return userDAO.get();
    }

    @Override
    public UserDao add(AddUserRequestDto requestDTO) {
        if (userRepository.isEmailExist(requestDTO.email())) {
            throw new UserAlreadyFoundException(requestDTO.email());
        }

        UserDao userDAO = new UserDao(UUID.randomUUID().toString(),
                requestDTO.firstName(),
                requestDTO.lastName(),
                requestDTO.email(),
                requestDTO.phone());

        return userRepository.save(userDAO);
    }


    @Override
    public UserDao updateById(UpdateUserRequestDto updateUserRequestDTO) {
        UserDao userDAO = findByIdOrThrowException(updateUserRequestDTO.id());

        userDAO.setFirstName(updateUserRequestDTO.firstName());
        userDAO.setLastName(updateUserRequestDTO.lastName());
        userDAO.setPhone(updateUserRequestDTO.phone());

        return userRepository.save(userDAO);
    }

    @Override
    public void deleteBy(String id) {
        userRepository.delete(findByIdOrThrowException(id));
    }

    private UserDao findByIdOrThrowException(String id) {
        Optional<UserDao> userDAO = userRepository.findById(id);
        if (userDAO.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return userDAO.get();
    }
}
