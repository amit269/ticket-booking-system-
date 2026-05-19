package com.ticketbooking.busservice.exception;

import com.ticketbooking.busservice.constants.ErrorCodeEnum;
import com.ticketbooking.busservice.pojo.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.ticketbooking.busservice.constants.ErrorCodeEnum.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleBusException(CustomException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }


    // if any exception comes that we didn't handle then spring catch or exception handler catch this
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(){
        ErrorResponse response   =new ErrorResponse(ErrorCodeEnum.GENERIC_ERROR.getCode(),
                ErrorCodeEnum.GENERIC_ERROR.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .findFirst()
                .orElse(ErrorCodeEnum.INVALID_INPUT.getMessage());

        ErrorResponse response = new ErrorResponse(
                ErrorCodeEnum.INVALID_INPUT.getCode(),
                message
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
