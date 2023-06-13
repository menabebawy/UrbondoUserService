package com.urbondo.user.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urbondo.user.api.controller.AddUserRequestDTO;
import com.urbondo.user.api.controller.UpdateUserRequestDTO;
import com.urbondo.user.api.controller.UserController;
import com.urbondo.user.api.controller.UserNotFoundException;
import com.urbondo.user.api.service.User;
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

    private static Stream<AddUserRequestDTO> provideAddUserRequestsForBadRequest() {
        return Stream.of(
                new AddUserRequestDTO("", "Json", "json@test.com", "123456789"),
                new AddUserRequestDTO("Json", "", "json@test.com", "123456789"),
                new AddUserRequestDTO("Json", "Tom", "json.test.com", "123456789"),
                new AddUserRequestDTO("Tom", "Cat", "json@test.com", "")
        );
    }

    @Test
    void whenGetUser_givenNotFoundUserId_thenReturnNotFoundCode() throws Exception {
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
    void whenPostUser_givenInvalidRequest_thenBadRequest(AddUserRequestDTO requestDTO) throws Exception {
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAddUser_givenValidRequest_thenCorrect() throws Exception {
        AddUserRequestDTO requestDTO = new AddUserRequestDTO(getUser().firstName(), getUser().lastName(), getUser().email(), getUser().phone());

        when(userService.add(requestDTO)).thenReturn(getUser());

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void whenUpdateUser_givenValidRequest_thenCorrect() throws Exception {
        UpdateUserRequestDTO requestDTO = new UpdateUserRequestDTO(getUser().id(), "Tim", getUser().lastName(), getUser().phone());
        User updatedUser = new User(getUser().id(), "Tim", getUser().lastName(), getUser().email(), getUser().phone());

        when(userService.updateById(requestDTO)).thenReturn(updatedUser);

        mockMvc.perform(patch(URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteUser_givenNotFoundUserId_thenBadRequest() throws Exception {
        doThrow(new UserNotFoundException(getUser().id())).when(userService).deleteBy(getUser().id());

        mockMvc.perform(delete(URL + "/" + USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    private User getUser() {
        return new User(USER_ID, "Tom", "Cat", "tom.cat@text.com", "123456789");
    }

}
