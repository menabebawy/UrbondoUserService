package com.urbondo.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urbondo.user.service.controller.UserController;
import com.urbondo.user.service.exception.UserNotFoundException;
import com.urbondo.user.service.model.AddUserRequestDTO;
import com.urbondo.user.service.model.AddUserResponseDTO;
import com.urbondo.user.service.model.UserDTO;
import com.urbondo.user.service.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserController.class})
class UserControllerTests {
    private final String BASE_URL = "/user";
    private final String USER_ID = "1";
    private final String NOT_FOUND_USER_ID = "123";
    private final String VALID_EMAIL = "valid@test.com";
    private final String VALID_PHONE = "1234567890";
    private final String VALID_FIRST_NAME = "Json";
    private final String VALID_LAST_NAME = "Tom";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;

    @Test
    void whenGetUserById_givenNotExistUserId_thenUserNotFoundException() throws Exception {
        when(userService.findById(NOT_FOUND_USER_ID)).thenThrow(new UserNotFoundException(NOT_FOUND_USER_ID));

        mockMvc.perform(get(BASE_URL + "/" + NOT_FOUND_USER_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.is("User id:" + NOT_FOUND_USER_ID + " is not found.")));
    }

    @Test
    void whenGetUserById_givenValidUserId_thenCorrect() throws Exception {
        when(userService.findById(USER_ID)).thenReturn(userDTO());

        mockMvc.perform(get(BASE_URL + "/" + USER_ID).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(userDTO().id())))
                .andExpect(jsonPath("$.firstName", Matchers.is(userDTO().firstName())))
                .andExpect(jsonPath("$.lastName", Matchers.is(userDTO().lastName())))
                .andExpect(jsonPath("$.email", Matchers.is(userDTO().email())))
                .andExpect(jsonPath("$.phone", Matchers.is(userDTO().phone())));
    }

    @Test
    void whenPostNewUser_givenInvalidFields_thenBadRequest() throws Exception {
        // invalid email
        AddUserRequestDTO invalidEmailRequestDTO = new AddUserRequestDTO(VALID_FIRST_NAME,
                                                                         VALID_LAST_NAME,
                                                                         "invalid.email.com",
                                                                         VALID_PHONE);

        mockMvc.perform(post(BASE_URL).contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidEmailRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("email is invalid.")));

        // invalid lastName
        AddUserRequestDTO invalidFirstNameRequestDTO = new AddUserRequestDTO(VALID_FIRST_NAME,
                                                                             "",
                                                                             VALID_EMAIL,
                                                                             VALID_PHONE);

        mockMvc.perform(post(BASE_URL).contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidFirstNameRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("lastName must not be blank.")));
    }

    @Test
    void whenPostNewUser_givenValidRequestBody_thenGetId() throws Exception {
        AddUserRequestDTO requestDTO = new AddUserRequestDTO(VALID_FIRST_NAME,
                                                             VALID_LAST_NAME,
                                                             VALID_EMAIL,
                                                             VALID_PHONE);

        doReturn(new AddUserResponseDTO(USER_ID)).when(userService).add(any());

        when(userService.add(requestDTO)).thenReturn(new AddUserResponseDTO(USER_ID));


        mockMvc.perform(post(BASE_URL).contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(USER_ID)));
    }

    private UserDTO userDTO() {
        return new UserDTO(USER_ID, VALID_FIRST_NAME, VALID_LAST_NAME, VALID_EMAIL, VALID_PHONE);
    }
}