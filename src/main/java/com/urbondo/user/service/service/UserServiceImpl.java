package com.urbondo.user.service.service;

import com.urbondo.user.service.exception.UserAlreadyFoundException;
import com.urbondo.user.service.exception.UserNotFoundException;
import com.urbondo.user.service.model.*;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

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
    public AddUserResponseDTO add(final AddUserRequestDTO requestDTO) {
        if (userRepository.findByEmail(requestDTO.getEmail()) != null) {
            throw new UserAlreadyFoundException(requestDTO.getEmail());
        }

        UserEntity userEntity = userRepository.save(requestDTO.transferToUserEntity());
        return new AddUserResponseDTO(userEntity.getId());
    }


    @Override
    public UserDTO updateBy(final UpdateUserRequestDTO requestDTO) {
        return Stream.of(findById(requestDTO.getId()))
                .map(Mapper::transferToUserEntity)
                .map(userRepository::save)
                .map(Mapper::transferToUserDTO)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public void deleteBy(String id) {
        findById(id);
        userRepository.deleteById(id);
    }
}
