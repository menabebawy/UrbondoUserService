package com.urbondo.user.service;

import com.urbondo.user.service.model.AddUserRequestDTO;
import com.urbondo.user.service.model.UserDTO;

class MockData {

    static final String VALID_EMAIL = "valid@test.com";
    static final String VALID_PHONE = "1234567890";
    static final String VALID_FIRST_NAME = "Json";
    static final String VALID_LAST_NAME = "Tom";
    final static String USER_ID = "1";
    final static String NOT_FOUND_USER_ID = "123";

    static UserDTO userDTO() {
        return new UserDTO(USER_ID, VALID_FIRST_NAME, VALID_LAST_NAME, VALID_EMAIL, VALID_PHONE);
    }

    static AddUserRequestDTO getAddUserRequestDTO() {
        return new AddUserRequestDTO(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_EMAIL, VALID_PHONE);
    }
}
