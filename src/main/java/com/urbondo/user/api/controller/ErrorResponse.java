package com.urbondo.user.api.controller;

import org.springframework.http.HttpStatus;

import java.util.Date;

class ErrorResponse {
    private final Date timestamp;
    private final int code;
    private final String status;
    private final String message;

    ErrorResponse(HttpStatus httpStatus, String message) {
        this.timestamp = new Date();
        this.code = httpStatus.value();
        this.status = httpStatus.name();
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
