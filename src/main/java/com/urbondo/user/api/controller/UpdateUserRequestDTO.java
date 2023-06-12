package com.urbondo.user.api.controller;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequestDTO(@NotBlank String id,
                                   @NotBlank String firstName,
                                   @NotBlank String lastName,
                                   @NotBlank String phone) {
}
