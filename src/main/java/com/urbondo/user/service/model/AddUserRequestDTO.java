package com.urbondo.user.service.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AddUserRequestDTO {
    @NotBlank
    protected final String firstName;

    @NotBlank
    protected final String lastName;

    @NotBlank
    @Email
    protected final String email;

    @Pattern(regexp = "(^$|[0-9]{10})")
    protected final String phone;

    public AddUserRequestDTO(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public UserEntity transferToUserEntity() {
        return new UserEntity(null, firstName, lastName, email, phone);
    }

    public String getEmail() {
        return email;
    }
}
