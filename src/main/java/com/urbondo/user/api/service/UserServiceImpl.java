package com.urbondo.user.api.service;

import com.urbondo.user.api.controller.AddUserRequestDTO;
import com.urbondo.user.api.controller.UpdateUserRequestDTO;
import com.urbondo.user.api.controller.UserAlreadyFoundException;
import com.urbondo.user.api.controller.UserNotFoundException;
import com.urbondo.user.api.repository.UserDAO;
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
    public User findById(String id) {
        return findOptionalDaoById(id).map(this::transferToUser).orElse(null);
    }

    @Override
    public User add(AddUserRequestDTO requestDTO) {
        if (userRepository.isEmailExist(requestDTO.email())) {
            throw new UserAlreadyFoundException(requestDTO.email());
        }

        UserDAO userDAO = new UserDAO(UUID.randomUUID().toString(),
                requestDTO.firstName(),
                requestDTO.lastName(),
                requestDTO.email(),
                requestDTO.phone());

        userRepository.save(userDAO);

        return transferToUser(userDAO);
    }


    @Override
    public User updateById(UpdateUserRequestDTO updateUserRequestDTO) {
        UserDAO userDAO = findByIdOrThrowException(updateUserRequestDTO.id());

        userDAO.setFirstName(updateUserRequestDTO.firstName());
        userDAO.setLastName(updateUserRequestDTO.lastName());
        userDAO.setPhone(updateUserRequestDTO.phone());

        userRepository.save(userDAO);

        return transferToUser(userDAO);
    }

    @Override
    public void deleteBy(String id) {
        userRepository.delete(findByIdOrThrowException(id));
    }

    private UserDAO findByIdOrThrowException(String id) {
        Optional<UserDAO> userDAO = findOptionalDaoById(id);
        if (userDAO.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return userDAO.get();
    }

    private Optional<UserDAO> findOptionalDaoById(String id) {
        UserDAO userDAO = userRepository.findById(id);
        if (userDAO == null) {
            return Optional.empty();
        }
        return Optional.of(userDAO);
    }

    private User transferToUser(UserDAO userDAO) {
        return new User(userDAO.getId(),
                userDAO.getFirstName(),
                userDAO.getLastName(),
                userDAO.getEmail(),
                userDAO.getPhone());
    }
}
