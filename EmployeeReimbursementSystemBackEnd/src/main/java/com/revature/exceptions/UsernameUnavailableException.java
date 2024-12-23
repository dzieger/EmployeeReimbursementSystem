package com.revature.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UsernameUnavailableException extends RuntimeException {

    public UsernameUnavailableException(String message) {
        super(message);
    }

}
