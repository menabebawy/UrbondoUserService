package com.urbondo.user.service.service;

import com.urbondo.user.service.exception.UserAlreadyFoundException;
import com.urbondo.user.service.exception.UserNotFoundException;
import com.urbondo.user.service.repository.UserDAO;
import com.urbondo.user.service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(final String id) {
        UserDAO userDAO = findByIdOrThrowException(id);
        return transferToUser(userDAO);
    }

    @Override
    public String add(final AddUser addUser) {
        if (userRepository.isEmailExist(addUser.email())) {
            throw new UserAlreadyFoundException(addUser.email());
        }

        UserDAO userDAO = new UserDAO(UUID.randomUUID().toString(),
                                      addUser.firstName(),
                                      addUser.lastName(),
                                      addUser.email(),
                                      addUser.phone());

        userRepository.save(userDAO);

        return userDAO.getId();
    }


    @Override
    public User updateById(final String id, final UpdateUser updateUser) {
        UserDAO userDAO = findByIdOrThrowException(id);

        userDAO.setFirstName(updateUser.firstName());
        userDAO.setLastName(updateUser.lastName());
        userDAO.setPhone(updateUser.phone());

        userRepository.save(userDAO);

        return transferToUser(userDAO);
    }

    @Override
    public void deleteBy(final String id) {
        UserDAO userDAO = findByIdOrThrowException(id);
        userRepository.delete(userDAO);
    }

    private UserDAO findByIdOrThrowException(String id) {
        UserDAO userDAO = userRepository.findById(id);
        if (userDAO == null) {
            throw new UserNotFoundException(id);
        }
        return userDAO;
    }

    private User transferToUser(final UserDAO userDAO) {
        return new User(userDAO.getId(),
                        userDAO.getFirstName(),
                        userDAO.getLastName(),
                        userDAO.getEmail(),
                        userDAO.getPhone());
    }
}
