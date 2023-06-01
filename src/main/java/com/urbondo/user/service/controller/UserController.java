package com.urbondo.user.service.controller;

import com.urbondo.user.service.service.User;
import com.urbondo.user.service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    User fetchUserById(@PathVariable @Valid final String id) {
        return userService.findById(id);
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    AddUserResponseDTO addNewUser(@RequestBody @Valid final AddUserRequestDTO requestDTO) {
        return new AddUserResponseDTO(userService.add(requestDTO.transferToAddUserDTO()));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(OK)
    User updateUser(@PathVariable @Valid final String id, @RequestBody @Valid final UpdateUserRequestDTO requestDTO) {
        return userService.updateById(id, requestDTO.transferToUpdateUser());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    void deleteUserById(@PathVariable @Valid final String id) {
        userService.deleteBy(id);
    }
}
