package com.ticketbooking.userservice.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class CustomException extends RuntimeException{
    private  final String errorCode;
    private  final HttpStatus httpStatus;

    public CustomException(String errorCode, String errorMessage, HttpStatus httpStatus) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
