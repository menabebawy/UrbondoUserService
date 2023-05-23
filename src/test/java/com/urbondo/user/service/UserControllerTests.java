package com.urbondo.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urbondo.user.service.controller.UserController;
import com.urbondo.user.service.exception.UserNotFoundException;
import com.urbondo.user.service.model.AddUserRequestDTO;
import com.urbondo.user.service.model.AddUserResponseDTO;
import com.urbondo.user.service.model.UpdateUserRequestDTO;
import com.urbondo.user.service.model.UserDTO;
import com.urbondo.user.service.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserController.class})
class UserControllerTests {
    private final static String BASE_URL = "/user";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;

    private static Stream<Arguments> addUserRequestAndErrorMessage() {
        return Stream.of(Arguments.of("email is invalid.",
                                      new AddUserRequestDTO(MockData.VALID_FIRST_NAME, MockData.VALID_LAST_NAME, ".com", MockData.VALID_PHONE)),
                         Arguments.of("lastName must not be blank.",
                                      new AddUserRequestDTO(MockData.VALID_FIRST_NAME, "", MockData.VALID_EMAIL, MockData.VALID_PHONE)),
                         Arguments.of("firstName must not be blank.",
                                      new AddUserRequestDTO("", MockData.VALID_LAST_NAME, MockData.VALID_EMAIL, MockData.VALID_PHONE)));
    }

    @Test
    void whenGetUserById_givenNotExistUserId_thenUserNotFoundException() throws Exception {
        when(userService.findById(MockData.NOT_FOUND_USER_ID)).thenThrow(new UserNotFoundException(MockData.NOT_FOUND_USER_ID));

        mockMvc.perform(get(getNotFoundUserURL()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.is(getNotFoundUserErrorValue())));
    }

    @Test
    void whenGetUserById_givenValidUserId_thenCorrect() throws Exception {
        when(userService.findById(MockData.USER_ID)).thenReturn(MockData.userDTO());

        mockMvc.perform(get(BASE_URL + "/" + MockData.USER_ID).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(MockData.userDTO().id())))
                .andExpect(jsonPath("$.firstName", Matchers.is(MockData.userDTO().firstName())))
                .andExpect(jsonPath("$.lastName", Matchers.is(MockData.userDTO().lastName())))
                .andExpect(jsonPath("$.email", Matchers.is(MockData.userDTO().email())))
                .andExpect(jsonPath("$.phone", Matchers.is(MockData.userDTO().phone())));
    }

    @ParameterizedTest
    @DisplayName("test invalid add new user request.")
    @MethodSource("addUserRequestAndErrorMessage")
    void whenPostNewUser_givenInvalidField_thenBadRequest(String error, AddUserRequestDTO requestDTO) throws Exception {
        mockMvc.perform(post(BASE_URL).contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is(error)));
    }

    @Test
    void whenPostNewUser_givenValidRequestBody_thenGetUserId() throws Exception {
        AddUserRequestDTO requestDTO = MockData.getAddUserRequestDTO();

        doReturn(new AddUserResponseDTO(MockData.USER_ID)).when(userService).add(any());

        when(userService.add(requestDTO)).thenReturn(new AddUserResponseDTO(MockData.USER_ID));

        mockMvc.perform(post(BASE_URL).contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(MockData.USER_ID)));
    }

    @Test
    void whenPutUser_givenBlankUserId_thenBadRequest() throws Exception {
        UpdateUserRequestDTO requestDTO = new UpdateUserRequestDTO("",
                                                                   MockData.VALID_FIRST_NAME,
                                                                   MockData.VALID_LAST_NAME,
                                                                   MockData.VALID_EMAIL,
                                                                   MockData.VALID_PHONE);

        mockMvc.perform(put(BASE_URL).contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("id must not be blank.")));

    }

    @Test
    void whenPutUser_givenNotExistUserId_thenUserNotFoundErrorMessage() throws Exception {
        UpdateUserRequestDTO requestDTO = new UpdateUserRequestDTO(MockData.NOT_FOUND_USER_ID,
                                                                   MockData.VALID_FIRST_NAME,
                                                                   MockData.VALID_LAST_NAME,
                                                                   MockData.VALID_EMAIL,
                                                                   MockData.VALID_PHONE);

        doThrow(new UserNotFoundException(MockData.NOT_FOUND_USER_ID)).when(userService).updateBy(any());

        mockMvc.perform(put(BASE_URL).contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.is(getNotFoundUserErrorValue())));
    }

    @Test
    void whenPutUser_givenValidRequest_thenCorrect() throws Exception {
        UpdateUserRequestDTO requestDTO = new UpdateUserRequestDTO(MockData.USER_ID,
                                                                   "New_FName",
                                                                   MockData.VALID_LAST_NAME,
                                                                   MockData.VALID_EMAIL,
                                                                   MockData.VALID_PHONE);

        UserDTO responseDTO = new UserDTO(MockData.USER_ID, "New_FName", MockData.VALID_LAST_NAME, MockData.VALID_EMAIL, MockData.VALID_PHONE);

        doReturn(responseDTO).when(userService).updateBy(any());

        mockMvc.perform(put(BASE_URL).contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(requestDTO.getId())));
    }

    @Test
    void whenDeleteUser_givenNotExistUserId_thenUserNotFoundException() throws Exception {
        doThrow(new UserNotFoundException(MockData.NOT_FOUND_USER_ID)).when(userService).deleteBy(MockData.NOT_FOUND_USER_ID);

        mockMvc.perform(delete(getNotFoundUserURL()).contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.is(getNotFoundUserErrorValue())));
    }

    @Test
    void whenDeleteUser_givenValidUserId_thenNoContent() throws Exception {
        doNothing().when(userService).deleteBy(MockData.USER_ID);

        mockMvc.perform(delete(getNotFoundUserURL()).contentType(APPLICATION_JSON)).andExpect(status().isNoContent());
    }

    private String getNotFoundUserURL() {
        return BASE_URL + "/" + MockData.NOT_FOUND_USER_ID;
    }

    private String getNotFoundUserErrorValue() {
        return "User id:" + MockData.NOT_FOUND_USER_ID + " is not found.";
    }

}