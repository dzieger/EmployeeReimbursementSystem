package com.revature.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReimbursementNotFoundException extends RuntimeException{

     public ReimbursementNotFoundException(String message) { super(message); }


}
