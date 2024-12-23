package com.revature.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UserDeleteFailedException extends RuntimeException{

    public UserDeleteFailedException(String message) { super(message); }

}
