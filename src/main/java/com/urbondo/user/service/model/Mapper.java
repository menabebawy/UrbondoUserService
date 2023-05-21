package com.urbondo.user.service.model;

public class Mapper {
    private Mapper() {}

    public static UserDTO transferToUserDTO(final UserEntity userEntity) {
        return new UserDTO(userEntity.id(),
                           userEntity.firstName(),
                           userEntity.lastName(),
                           userEntity.email(),
                           userEntity.phone());
    }

    public static UserEntity transferToUserEntity(final UserDTO userDTO) {
        return new UserEntity(userDTO.id(),
                              userDTO.firstName(),
                              userDTO.lastName(),
                              userDTO.email(),
                              userDTO.phone());
    }
}
