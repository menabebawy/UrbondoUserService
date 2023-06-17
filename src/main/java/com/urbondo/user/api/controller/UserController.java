package com.urbondo.user.api.controller;

import com.urbondo.user.api.repository.UserDao;
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
    public UserDao fetchUserById(@PathVariable @Valid String id) {
        return userService.findById(id);
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    public UserDao addNewUser(@RequestBody @Valid AddUserRequestDto requestDTO) {
        return userService.add(requestDTO);
    }

    @PatchMapping()
    @ResponseStatus(OK)
    public UserDao updateUser(@RequestBody @Valid UpdateUserRequestDto requestDTO) {
        return userService.updateById(requestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteUserById(@PathVariable @Valid String id) {
        userService.deleteBy(id);
    }
}
