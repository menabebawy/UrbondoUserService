package com.urbondo.user.service.service;

import com.urbondo.user.service.exception.UserAlreadyFoundException;
import com.urbondo.user.service.exception.UserNotFoundException;
import com.urbondo.user.service.model.AddUserRequestDTO;
import com.urbondo.user.service.model.Mapper;
import com.urbondo.user.service.model.UpdateUserRequestDTO;
import com.urbondo.user.service.model.UserDTO;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO findById(String id) {
        return userRepository.findById(id)
                .stream()
                .findFirst()
                .map(Mapper::transferToUserDTO)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public void add(final AddUserRequestDTO requestDTO) {
        if (isAlreadyExist(requestDTO.getEmail())) {
            throw new UserAlreadyFoundException(requestDTO.getEmail());
        }

        userRepository.save(requestDTO.transferToUserEntity());
    }

    private boolean isAlreadyExist(String email) {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .anyMatch(userEntity -> userEntity.email().equals(email));
    }


    @Override
    public UserDTO updateBy(final UpdateUserRequestDTO requestDTO) {
        return Stream.of(findById(requestDTO.getId()))
                .map(Mapper::transferToUserEntity)
                .map(userRepository::save)
                .map(Mapper::transferToUserDTO)
                .findFirst()
                .get();
    }

    @Override
    public void deleteBy(String id) {
        findById(id);
        userRepository.deleteById(id);
    }
}
