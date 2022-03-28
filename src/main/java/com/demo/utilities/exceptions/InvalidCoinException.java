package com.demo.utilities.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidCoinException  extends Exception {
    public InvalidCoinException(String message) {
        super(message);
    }
}

