package com.urbondo.user.service.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public class AddUserRequestDTO {
    @NotBlank(message = "firstName must not be blank.")
    protected final String firstName;

    @NotBlank(message = "lastName must not be blank.")
    protected final String lastName;

    @NotBlank(message = "email must not be blank.")
    @Email(message = "email is invalid.")
    protected final String email;

    @NotBlank(message = "phone must not be blank.")
    @Pattern(regexp = "^\\d{10}$", message = "phone is invalid.")
    protected final String phone;

    public AddUserRequestDTO(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public UserEntity transferToUserEntity() {
        return new UserEntity(UUID.randomUUID().toString(), firstName, lastName, email, phone);
    }

    public String getEmail() {
        return email;
    }
}
