package com.urbondo.user.service;

import com.urbondo.user.service.controller.AddUserRequestDTO;
import com.urbondo.user.service.controller.AddUserResponseDTO;
import com.urbondo.user.service.controller.UpdateUserRequestDTO;
import com.urbondo.user.service.exception.UserAlreadyFoundException;
import com.urbondo.user.service.exception.UserNotFoundException;
import com.urbondo.user.service.repository.UserDAO;
import com.urbondo.user.service.service.User;
import com.urbondo.user.service.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static com.urbondo.user.service.MockData.*;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
class UserServiceImplTests {
    @Mock
    private UserRepository userRepository;
    private UserService userService;

    static UserDAO getUserEntity() {
        return new UserDAO(USER_ID, VALID_FIRST_NAME, VALID_LAST_NAME, VALID_EMAIL, VALID_PHONE);
    }

    @BeforeEach
    void setup() {
//        userService = new UserServiceImpl(n);
    }

    @AfterEach
    void destroy() {
        userService = null;
    }

    @Test
    void whenGetUserById_givenNotFoundUserId_thenUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> userService.findById(USER_ID));
    }

    @Test
    void whenGetUserById_givenExistUserId_thenGetUserData() {
        UserDAO userDAO = getUserEntity();

//        doReturn(Optional.of(userEntity)).when(userRepository).findById(USER_ID);

        User user = userService.findById(USER_ID);

        assertEquals(USER_ID, user.id());
        assertEquals(VALID_FIRST_NAME, user.firstName());
        assertEquals(VALID_LAST_NAME, user.lastName());
        assertEquals(VALID_EMAIL, user.email());
        assertEquals(VALID_PHONE, user.phone());
    }

    @Test
    void whenAddNewUser_givenExistEmail_thenUserAlreadyFoundException() {
        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(getUserEntity());

        AddUserRequestDTO requestDTO = getAddUserRequestDTO();

        assertThrows(UserAlreadyFoundException.class, () -> userService.add(requestDTO));
    }

    @Test
    void whenAddNewUser_givenValidRequest_thenGetUserId() {
        AddUserRequestDTO requestDTO = getAddUserRequestDTO();

        when(userRepository.save(any())).thenReturn(getUserEntity());

        AddUserResponseDTO responseDTO = userService.add(requestDTO);

        assertNotNull(responseDTO.id());
    }

    @Test
    void whenUpdateUserById_givenNotExistUserId_thenUserNotFoundException() {
        UpdateUserRequestDTO requestDTO = getNotExistUpdateUserRequestDTO();
        assertThrows(UserNotFoundException.class, () -> userService.updateBy(requestDTO));
    }

    @Test
    void whenDeleteUserById_givenNotExistUserId_thenUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> userService.deleteBy(NOT_FOUND_USER_ID));
    }
}
