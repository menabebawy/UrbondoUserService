package com.urbondo.user.service.model;

public final class Mapper {
    private Mapper() {}

    public static UserDTO transferToUserDTO(final UserEntity userEntity) {
        return new UserDTO(userEntity.getId(),
                           userEntity.getFirstName(),
                           userEntity.getLastName(),
                           userEntity.getEmail(),
                           userEntity.getPhone());
    }

    public static UserEntity transferToUserEntity(final UserDTO userDTO) {
        return new UserEntity(userDTO.id(),
                              userDTO.firstName(),
                              userDTO.lastName(),
                              userDTO.email(),
                              userDTO.phone());
    }
}
