package com.urbondo.user.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urbondo.lib.ResourceNotFoundException;
import com.urbondo.user.api.controller.AddUserRequestDto;
import com.urbondo.user.api.controller.UpdateUserRequestDto;
import com.urbondo.user.api.controller.UserController;
import com.urbondo.user.api.controller.UserNotFoundException;
import com.urbondo.user.api.repository.UserDao;
import com.urbondo.user.api.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserController.class})
class UserControllerTests {
    private final static String URL = "/user";
    private final static String USER_ID = "7958e713-efb6-4661-8cb2-d92d25730232";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;

    private static Stream<AddUserRequestDto> provideAddUserRequestsForBadRequest() {
        return Stream.of(
                new AddUserRequestDto("", "Json", "json@test.com", "123456789"),
                new AddUserRequestDto("Json", "", "json@test.com", "123456789"),
                new AddUserRequestDto("Json", "Tom", "json.test.com", "123456789"),
                new AddUserRequestDto("Tom", "Cat", "json@test.com", "")
        );
    }

    @Test
    void whenGetUser_givenNotFoundUserId_thenReturnNotFoundCode() throws Exception {
        when(userService.findById("2")).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(get(URL + "/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetUser_givenExistUserId_thenCorrect() throws Exception {
        when(userService.findById(USER_ID)).thenReturn(getUser());

        mockMvc.perform(get(URL + "/" + USER_ID).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("provideAddUserRequestsForBadRequest")
    void whenPostUser_givenInvalidRequest_thenBadRequest(AddUserRequestDto requestDTO) throws Exception {
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAddUser_givenValidRequest_thenCorrect() throws Exception {
        AddUserRequestDto requestDTO = new AddUserRequestDto(getUser().getFirstName(), getUser().getLastName(), getUser().getEmail(), getUser().getPhone());

        when(userService.add(requestDTO)).thenReturn(getUser());

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void whenUpdateUser_givenValidRequest_thenCorrect() throws Exception {
        UpdateUserRequestDto requestDTO = new UpdateUserRequestDto(getUser().getId(), "Tim", getUser().getEmail(), getUser().getPhone());
        UserDao updatedUser = new UserDao(getUser().getId(), "Tim", getUser().getLastName(), getUser().getEmail(), getUser().getPhone());

        when(userService.updateById(requestDTO)).thenReturn(updatedUser);

        mockMvc.perform(patch(URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteUser_givenNotFoundUserId_thenBadRequest() throws Exception {
        doThrow(new UserNotFoundException(USER_ID)).when(userService).deleteBy(getUser().getId());

        mockMvc.perform(delete(URL + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    private UserDao getUser() {
        return new UserDao(USER_ID, "Tom", "Cat", "tom.cat@text.com", "123456789");
    }

}
