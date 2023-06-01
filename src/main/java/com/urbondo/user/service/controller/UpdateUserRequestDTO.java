package com.urbondo.user.service.controller;

import com.urbondo.user.service.service.UpdateUser;
import jakarta.validation.constraints.NotBlank;

record UpdateUserRequestDTO(@NotBlank String firstName,
                            @NotBlank String lastName,
                            @NotBlank String phone) {
    public UpdateUser transferToUpdateUser() {
        return new UpdateUser(firstName, lastName, phone);
    }
}
