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
}
