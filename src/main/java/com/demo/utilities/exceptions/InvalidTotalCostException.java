package com.demo.utilities.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidTotalCostException  extends Exception {
    public InvalidTotalCostException(String message) {
        super(message);
    }
}

