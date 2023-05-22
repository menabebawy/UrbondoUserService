package com.urbondo.user.service;

import com.urbondo.user.service.exception.UserNotFoundException;
import com.urbondo.user.service.model.UserDTO;
import com.urbondo.user.service.model.UserEntity;
import com.urbondo.user.service.service.UserRepository;
import com.urbondo.user.service.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserControllerTests.class})
class UserControllerTests {
    private final String BASE_URL = "/user";
    private final String USER_ID = "1";
    private final String USER_URL = BASE_URL + "/" + USER_ID;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;
    @MockBean
    UserRepository userRepository;

    @Test
    void whenGetUserById_givenNotExistUserId_thenUserNotFoundException() throws Exception {
        when(userService.findById("123")).thenThrow(new UserNotFoundException("123"));

        mockMvc.perform(get(BASE_URL + "/123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.is("")));
        ;
    }

    @Test
    void whenGetUserById_givenValidUserId_thenCorrect() throws Exception {
        when(userService.findById(USER_ID)).thenReturn(userDTO());
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity()));

        mockMvc.perform(get(USER_URL).contentType(APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    private UserEntity userEntity() {
        return new UserEntity(USER_ID, "", "", "", "");
    }

    private UserDTO userDTO() {
        return new UserDTO(USER_ID, "", "", "", "");
    }
}