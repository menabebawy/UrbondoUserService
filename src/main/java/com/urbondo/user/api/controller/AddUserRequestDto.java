package com.urbondo.user.api.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AddUserRequestDto(@NotBlank String firstName,
                                @NotBlank String lastName,
                                @NotBlank @Email String email,
                                @NotBlank String phone) {
}
