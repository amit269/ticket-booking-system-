package com.ticketbooking.busservice.exception;

import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Struct;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class CustomException extends  RuntimeException {
    private String errorCode;
    private HttpStatus httpStatus;

    public CustomException(String errorCode, String errorMessage, HttpStatus httpStatus) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
