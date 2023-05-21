package com.urbondo.user.service.controller;

import com.urbondo.user.service.model.AddUserRequestDTO;
import com.urbondo.user.service.model.UpdateUserRequestDTO;
import com.urbondo.user.service.model.UserDTO;
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
    UserDTO fetchUserById(@PathVariable @Valid final String id) {
        return userService.findById(id);
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    void addNewUser(@RequestBody @Valid final AddUserRequestDTO requestDTO) {
        userService.add(requestDTO);
    }

    @PutMapping()
    @ResponseStatus(OK)
    UserDTO updateUser(@RequestBody @Valid final UpdateUserRequestDTO requestDTO) {
        return userService.updateBy(requestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    void deleteUserById(@PathVariable @Valid final String id) {
        userService.deleteBy(id);
    }
}
