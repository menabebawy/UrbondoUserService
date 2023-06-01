package com.urbondo.user.service.controller;

import com.urbondo.user.service.service.AddUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

class AddUserRequestDTO {
    @NotBlank
    private final String firstName;

    @NotBlank
    private final String lastName;

    @NotBlank
    @Email
    private final String email;

    @NotBlank
    @Pattern(regexp = "^\\d{10}$")
    private final String phone;

    public AddUserRequestDTO(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public AddUser transferToAddUserDTO() {
        return new AddUser(firstName, lastName, email, phone);
    }
}
