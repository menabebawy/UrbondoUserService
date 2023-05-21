package com.urbondo.user.service.model;

import jakarta.validation.constraints.NotBlank;

public class UpdateUserRequestDTO extends AddUserRequestDTO {
    @NotBlank
    private final String id;

    public UpdateUserRequestDTO(String id, String firstName, String lastName, String email, String phone) {
        super(firstName, lastName, email, phone);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public UserEntity transferToUserEntity() {
        return new UserEntity(id, firstName, lastName, email, phone);
    }
}
