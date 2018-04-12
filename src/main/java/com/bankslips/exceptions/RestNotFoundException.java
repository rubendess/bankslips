package com.bankslips.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RestNotFoundException extends RuntimeException {

    public RestNotFoundException() { super(); }

    public RestNotFoundException(String exception) {
        super(exception);
    }
}
