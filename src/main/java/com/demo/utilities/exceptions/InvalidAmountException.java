package com.demo.utilities.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidAmountException  extends Exception {
    public InvalidAmountException(String message) {
        super(message);
    }
}

