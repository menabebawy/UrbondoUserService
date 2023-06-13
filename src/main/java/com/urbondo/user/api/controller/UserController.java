package com.urbondo.user.api.controller;

import com.urbondo.user.api.service.User;
import com.urbondo.user.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public User fetchUserById(@PathVariable @Valid String id) {
        User user = userService.findById(id);
        if (user == null) {
            throw new ResourceNotFoundException();
        }
        return user;
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    public User addNewUser(@RequestBody @Valid AddUserRequestDTO requestDTO) {
        return userService.add(requestDTO);
    }

    @PatchMapping()
    @ResponseStatus(OK)
    public User updateUser(@RequestBody @Valid UpdateUserRequestDTO requestDTO) {
        return userService.updateById(requestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteUserById(@PathVariable @Valid String id) {
        userService.deleteBy(id);
    }
}
