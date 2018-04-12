package com.bankslips.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class RestInvalidItemException extends RuntimeException {

    public RestInvalidItemException() { super(); }

    public RestInvalidItemException(String exception) {
        super(exception);
    }
}
